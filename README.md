[English](README.md) | [中文](README_CN.md)

# Flight Kick Bypass

![Flight Kick Bypass icon](https://github.com/Henryxjh/flight-kick-bypass/blob/1.21.1-neoforge/src/main/resources/flightkickbypass.png?raw=true)

Flight Kick Bypass is a server-side Fabric and NeoForge mod for Minecraft 1.21.1. The same universal jar supports both loaders.

When a player is about to be disconnected by the vanilla server for floating or flying too long, the mod first teleports the player to the top of the first solid collision block below their current position. After that, it either continues the vanilla kick flow or keeps the player online, depending on the server config.

## Why

Some modded blocks can have models or collision behavior that leave players in positions where the server repeatedly triggers the vanilla flying check. Network issues can cause similar repeated disconnects. Once this happens, the player may be kicked again immediately after reconnecting, which often requires an administrator to manually move the player or intervene through server tools.

This mod reduces that recovery work by moving the player to a valid block below them before the flying kick is processed.

## Features

- Handles the vanilla `multiplayer.disconnect.flying` kick path.
- Teleports the player to the nearest valid block below their current X/Z position before the kick is processed.
- Resets velocity and fall distance after teleporting.
- Supports both normal player floating kicks and vehicle floating kicks.
- Can be configured to keep the player online after a successful teleport.
- Can expand the landing search to a configurable square radius if no block is found directly below the player.
- Logs the loaded config state during config load and reload.
- Ships Fabric and NeoForge support in one jar while keeping the landing and mixin implementation in shared source code.

## Requirements

- Minecraft: `1.21.1`
- Loader: Fabric Loader or NeoForge
- Java: `21`
- Server-side logic; multiplayer clients do not need the mod
- Fabric API is not required

## Configuration

Both loader implementations use the same keys and defaults.

NeoForge writes its server config to `<world>/serverconfig/flightkickbypass-server.toml`:

```toml
kickAfterTeleport = true
expandedSearchRadius = 0
disconnectMessageSuffix = "Teleported to the nearest safe position before disconnecting."
teleportMessage = "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected."
```

Fabric writes `config/flightkickbypass.json`:

```json
{
  "kickAfterTeleport": true,
  "expandedSearchRadius": 0,
  "disconnectMessageSuffix": "Teleported to the nearest safe position before disconnecting.",
  "teleportMessage": "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected."
}
```

`kickAfterTeleport` options:

- `true` default: teleport the player to the ground, then continue the vanilla kick flow.
- `false`: if the teleport succeeds, cancel the kick and reset the floating counter. If no valid block is found below the player, the vanilla kick still happens.

`expandedSearchRadius` options:

- `0` default: only search directly below the player.
- Greater than `0`: if the direct search fails, search a square area around the player's current block position. A value of `r` searches a `(2r + 1) x (2r + 1)` square.

Player-facing messages:

- `disconnectMessageSuffix`: appended on a new line to the flying disconnect reason after a successful teleport when `kickAfterTeleport` is `true`. Set it to an empty string to disable the extra line.
- `teleportMessage`: sent as a system message to the player after a successful teleport when `kickAfterTeleport` is `false`. Set it to an empty string to disable the message.

## Installation

1. Install Fabric Loader or NeoForge for Minecraft 1.21.1 on the server.
2. Put the same universal mod jar into the server `mods` directory.
3. Start the server once to generate the config.
4. Edit the generated config if needed. Restart Fabric after editing; NeoForge can also apply a supported server-config reload.

## Building

```sh
./gradlew build
```

The built jar is written to:

```text
build/libs/flightkickbypass-1.21.1-fabric-neoforge-<version>.jar
```

## Notes

This mod does not grant flight permission and does not change the vanilla movement check itself. It only changes what happens immediately before the vanilla flying disconnect is processed.

For a dedicated server, install the jar only on the server. To use it in a single-player integrated server, install the same jar in that client instance.

The project icon is AI-generated illustrative artwork, not an in-game screenshot.
