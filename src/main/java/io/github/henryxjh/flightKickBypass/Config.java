package io.github.henryxjh.flightKickBypass;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue KICK_AFTER_TELEPORT;
    private static final ModConfigSpec.IntValue EXPANDED_SEARCH_RADIUS;
    private static final ModConfigSpec.ConfigValue<String> DISCONNECT_MESSAGE_SUFFIX;
    private static final ModConfigSpec.ConfigValue<String> TELEPORT_MESSAGE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        KICK_AFTER_TELEPORT = builder
            .comment("Whether players should still be kicked after being teleported to the ground for floating too long.")
            .define("kickAfterTeleport", true);
        EXPANDED_SEARCH_RADIUS = builder
            .comment("Square search radius used when no landing block is found directly below the player. Set to 0 to disable expanded search.")
            .defineInRange("expandedSearchRadius", 0, 0, 64);
        DISCONNECT_MESSAGE_SUFFIX = builder
            .comment("Message appended to the flying disconnect reason after a successful teleport. Set to an empty string to disable the extra line.")
            .define("disconnectMessageSuffix", "Teleported to the nearest safe position before disconnecting.");
        TELEPORT_MESSAGE = builder
            .comment("Message sent to the player after a successful teleport when kickAfterTeleport is false. Set to an empty string to disable the message.")
            .define("teleportMessage", "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected.");
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

    public static String disconnectMessageSuffix() {
        return DISCONNECT_MESSAGE_SUFFIX.get();
    }

    public static String teleportMessage() {
        return TELEPORT_MESSAGE.get();
    }
}
