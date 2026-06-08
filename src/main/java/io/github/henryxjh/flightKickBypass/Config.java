package io.github.henryxjh.flightKickBypass;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue KICK_AFTER_TELEPORT;
    private static final ModConfigSpec.IntValue EXPANDED_SEARCH_RADIUS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        KICK_AFTER_TELEPORT = builder
            .comment("Whether players should still be kicked after being teleported to the ground for floating too long.")
            .define("kickAfterTeleport", true);
        EXPANDED_SEARCH_RADIUS = builder
            .comment("Square search radius used when no landing block is found directly below the player. Set to 0 to disable expanded search.")
            .defineInRange("expandedSearchRadius", 0, 0, 64);
        SPEC = builder.build();
    }

    private Config() {
    }

    public static boolean kickAfterTeleport() {
        return KICK_AFTER_TELEPORT.get();
    }

    public static int expandedSearchRadius() {
        return EXPANDED_SEARCH_RADIUS.get();
    }
}
