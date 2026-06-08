[English](README.md) | [中文](README_CN.md)

# Flight Kick Bypass

![Flight Kick Bypass icon](https://github.com/Henryxjh/flight-kick-bypass/blob/1.21.1-neoforge/src/main/resources/flightkickbypass.png?raw=true)

Flight Kick Bypass is a server-side NeoForge mod for Minecraft 1.21.1.

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

## Requirements

- Minecraft: `1.21.1`
- Loader: NeoForge
- Java: `21`
- Server side only

## Configuration

The mod registers a server config. On a dedicated server, NeoForge writes it under the world/server config directory.

```toml
kickAfterTeleport = true
expandedSearchRadius = 0
```

`kickAfterTeleport` options:

- `true` default: teleport the player to the ground, then continue the vanilla kick flow.
- `false`: if the teleport succeeds, cancel the kick and reset the floating counter. If no valid block is found below the player, the vanilla kick still happens.

`expandedSearchRadius` options:

- `0` default: only search directly below the player.
- Greater than `0`: if the direct search fails, search a square area around the player's current block position. A value of `r` searches a `(2r + 1) x (2r + 1)` square.

## Installation

1. Install NeoForge for Minecraft 1.21.1 on the server.
2. Put the mod jar into the server `mods` directory.
3. Start the server once to generate the config.
4. Edit the generated config if needed, then restart or reload the server config.

## Building

```sh
./gradlew build
```

The built jar is written to:

```text
build/libs/
```

## Notes

This mod does not grant flight permission and does not change the vanilla movement check itself. It only changes what happens immediately before the vanilla flying disconnect is processed.

The mod icon was generated with AI.
