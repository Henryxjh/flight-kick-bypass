package io.github.henryxjh.flightKickBypass;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(value = FlightKickBypass.MODID, dist = Dist.DEDICATED_SERVER)
public class FlightKickBypass {
    public static final String MODID = "flightkickbypass";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FlightKickBypass(IEventBus modEventBus, ModContainer modContainer) {
    }
}
