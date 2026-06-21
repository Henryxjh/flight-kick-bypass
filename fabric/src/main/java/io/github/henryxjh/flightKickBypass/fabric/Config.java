package io.github.henryxjh.flightKickBypass.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public final class Config {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("flightkickbypass.json");

    private static boolean kickAfterTeleport = true;
    private static int expandedSearchRadius;
    private static String disconnectMessageSuffix =
        "Teleported to the nearest safe position before disconnecting.";
    private static String teleportMessage =
        "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected.";

    private Config() {
    }

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            saveDefaults();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            kickAfterTeleport = readBoolean(config, "kickAfterTeleport", kickAfterTeleport);
            expandedSearchRadius = Math.clamp(
                readInt(config, "expandedSearchRadius", expandedSearchRadius),
                0,
                64
            );
            disconnectMessageSuffix = readString(
                config,
                "disconnectMessageSuffix",
                disconnectMessageSuffix
            );
            teleportMessage = readString(config, "teleportMessage", teleportMessage);
        } catch (IOException | RuntimeException exception) {
            LOGGER.error("Failed to load Fabric config from {}", CONFIG_PATH, exception);
        }
    }

    private static void saveDefaults() {
        JsonObject config = new JsonObject();
        config.addProperty("kickAfterTeleport", kickAfterTeleport);
        config.addProperty("expandedSearchRadius", expandedSearchRadius);
        config.addProperty("disconnectMessageSuffix", disconnectMessageSuffix);
        config.addProperty("teleportMessage", teleportMessage);

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(config) + System.lineSeparator(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            LOGGER.error("Failed to create Fabric config at {}", CONFIG_PATH, exception);
        }
    }

    private static boolean readBoolean(JsonObject config, String key, boolean fallback) {
        JsonElement value = config.get(key);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean()
            ? value.getAsBoolean()
            : fallback;
    }

    private static int readInt(JsonObject config, String key, int fallback) {
        JsonElement value = config.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            return fallback;
        }

        try {
            return value.getAsInt();
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static String readString(JsonObject config, String key, String fallback) {
        JsonElement value = config.get(key);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()
            ? value.getAsString()
            : fallback;
    }

    public static boolean kickAfterTeleport() {
        return kickAfterTeleport;
    }

    public static int expandedSearchRadius() {
        return expandedSearchRadius;
    }

    public static String disconnectMessageSuffix() {
        return disconnectMessageSuffix;
    }

    public static String teleportMessage() {
        return teleportMessage;
    }
}
