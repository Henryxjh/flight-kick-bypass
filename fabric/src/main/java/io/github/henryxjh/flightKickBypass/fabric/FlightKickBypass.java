package io.github.henryxjh.flightKickBypass.fabric;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public final class FlightKickBypass implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        Config.load();
        LOGGER.info(
            "Flight Kick Bypass loaded on Fabric: kickAfterTeleport={}, expandedSearchRadius={}, disconnectMessageSuffix='{}', teleportMessage='{}'",
            Config.kickAfterTeleport(),
            Config.expandedSearchRadius(),
            Config.disconnectMessageSuffix(),
            Config.teleportMessage()
        );
    }
}
