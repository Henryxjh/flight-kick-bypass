package io.github.henryxjh.flightKickBypass;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class FlightKickLanding {
    private FlightKickLanding() {
    }

    public static Optional<Vec3> findLandingPosition(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        int minY = level.getMinBuildHeight();
        int startY = Math.min(Mth.floor(player.getY()) - 1, level.getMaxBuildHeight() - 1);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, startY, z);
        CollisionContext collisionContext = CollisionContext.of(player);
        Optional<Vec3> directLandingPosition = findLandingPositionInColumn(
            level,
            collisionContext,
            pos,
            x,
            z,
            player.getX(),
            player.getZ(),
            startY,
            minY
        );
        if (directLandingPosition.isPresent()) {
            return directLandingPosition;
        }

        int expandedSearchRadius = Config.expandedSearchRadius();
        for (int radius = 1; radius <= expandedSearchRadius; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) {
                        continue;
                    }

                    int searchX = x + dx;
                    int searchZ = z + dz;
                    Optional<Vec3> landingPosition = findLandingPositionInColumn(
                        level,
                        collisionContext,
                        pos,
                        searchX,
                        searchZ,
                        searchX + 0.5,
                        searchZ + 0.5,
                        startY,
                        minY
                    );
                    if (landingPosition.isPresent()) {
                        return landingPosition;
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static Optional<Vec3> findLandingPositionInColumn(
        ServerLevel level,
        CollisionContext collisionContext,
        BlockPos.MutableBlockPos pos,
        int blockX,
        int blockZ,
        double landingX,
        double landingZ,
        int startY,
        int minY
    ) {
        for (int y = startY; y >= minY; y--) {
            pos.set(blockX, y, blockZ);
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getCollisionShape(level, pos, collisionContext);
            if (!shape.isEmpty()) {
                return Optional.of(new Vec3(landingX, y + shape.max(Direction.Axis.Y), landingZ));
            }
        }

        return Optional.empty();
    }
}
