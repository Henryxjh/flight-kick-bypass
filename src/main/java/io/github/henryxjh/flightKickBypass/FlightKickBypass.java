package io.github.henryxjh.flightKickBypass;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;

@Mod(value = FlightKickBypass.MODID)
public class FlightKickBypass {
    public static final String MODID = "flightkickbypass";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FlightKickBypass(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modEventBus.addListener(this::onConfigLoaded);
        modEventBus.addListener(this::onConfigReloaded);
        LOGGER.info("Flight Kick Bypass loaded; waiting for server config.");
    }

    private void onConfigLoaded(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(MODID)) {
            logConfigState("loaded");
        }
    }

    private void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(MODID)) {
            logConfigState("reloaded");
        }
    }

    private static void logConfigState(String phase) {
        LOGGER.info(
            "Flight Kick Bypass config {}: kickAfterTeleport={}, expandedSearchRadius={}, disconnectMessageSuffix='{}', teleportMessage='{}'",
            phase,
            Config.kickAfterTeleport(),
            Config.expandedSearchRadius(),
            Config.disconnectMessageSuffix(),
            Config.teleportMessage()
        );
    }
}
