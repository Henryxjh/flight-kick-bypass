package io.github.henryxjh.flightKickBypass.mixin;

import com.mojang.logging.LogUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Unique
    private static final Logger flightkickbypass$LOGGER = LogUtils.getLogger();
    @Unique
    private static final String flightkickbypass$CONFIG = "io.github.henryxjh.flightKickBypass.Config";
    @Unique
    private static final String flightkickbypass$FLIGHT_KICK_LANDING = "io.github.henryxjh.flightKickBypass.FlightKickLanding";

    @Shadow
    public ServerPlayer player;

    @Shadow
    public abstract void teleport(double x, double y, double z, float yaw, float pitch);

    @Shadow
    private boolean clientIsFloating;

    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private boolean clientVehicleIsFloating;

    @Shadow
    private int aboveGroundVehicleTickCount;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void flightkickbypass$landBeforePlayerFlyingKick(CallbackInfo ci) {
        boolean landed = this.flightkickbypass$landPlayer();
        if (landed && !flightkickbypass$isKickAfterTeleportEnabled()) {
            this.clientIsFloating = false;
            this.aboveGroundTickCount = 0;
            ci.cancel();
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void flightkickbypass$landBeforeVehicleFlyingKick(CallbackInfo ci) {
        boolean landed = this.flightkickbypass$landPlayer();
        if (landed && !flightkickbypass$isKickAfterTeleportEnabled()) {
            this.clientVehicleIsFloating = false;
            this.aboveGroundVehicleTickCount = 0;
            ci.cancel();
        }
    }

    @Unique
    private boolean flightkickbypass$landPlayer() {
        return flightkickbypass$findLandingPosition(this.player)
            .map(this::flightkickbypass$teleportToLandingPosition)
            .orElse(false);
    }

    @Unique
    private boolean flightkickbypass$teleportToLandingPosition(Vec3 position) {
        this.teleport(position.x(), position.y(), position.z(), this.player.getYRot(), this.player.getXRot());
        this.player.setDeltaMovement(Vec3.ZERO);
        this.player.resetFallDistance();
        this.player.setOnGround(true);
        return true;
    }

    @Unique
    private static boolean flightkickbypass$isKickAfterTeleportEnabled() {
        return flightkickbypass$invokeStatic(
                flightkickbypass$CONFIG,
                "kickAfterTeleport",
                new Class<?>[0]
        ).filter(Boolean.class::isInstance).map(Boolean.class::cast).orElse(true);
    }

    @Unique
    private static Optional<Vec3> flightkickbypass$findLandingPosition(ServerPlayer player) {
        Optional<Object> result = flightkickbypass$invokeStatic(
                flightkickbypass$FLIGHT_KICK_LANDING,
                "findLandingPosition",
                new Class<?>[]{ServerPlayer.class},
                player
        );
        if (result.isEmpty() || !(result.get() instanceof Optional<?> position)) {
            return Optional.empty();
        }

        return position.filter(Vec3.class::isInstance).map(Vec3.class::cast);
    }

    @Unique
    private static Optional<Object> flightkickbypass$invokeStatic(
            String className,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments
    ) {
        try {
            Class<?> owner = Class.forName(className);
            Method method = owner.getMethod(methodName, parameterTypes);
            return Optional.ofNullable(method.invoke(null, arguments));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException exception) {
            flightkickbypass$LOGGER.error(
                    "Failed to access {}.{} from server game packet listener mixin",
                    className,
                    methodName,
                    exception
            );
            return Optional.empty();
        } catch (InvocationTargetException exception) {
            flightkickbypass$LOGGER.error(
                    "Failed to invoke {}.{} from server game packet listener mixin",
                    className,
                    methodName,
                    exception.getCause()
            );
            return Optional.empty();
        }
    }
}
