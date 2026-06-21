[English](README.md) | [中文](README_CN.md)

# Flight Kick Bypass

![Flight Kick Bypass 图标](https://github.com/Henryxjh/flight-kick-bypass/blob/1.21.1-neoforge/src/main/resources/flightkickbypass.png?raw=true)

Flight Kick Bypass 是一个适用于 Minecraft 1.21.1 的服务端 Fabric 和 NeoForge mod。同一个通用 jar 同时支持两个加载器。

当玩家即将因为原版服务器的“长时间悬空/飞行”检测而被踢出时，本 mod 会先把玩家传送到当前位置 X/Z 下方第一个具有碰撞体的方块顶部。传送完成后，可以根据服务端配置选择继续执行原版踢出流程，或保留玩家在线。

## 为什么做这个 mod

某些 mod 方块的模型或碰撞表现，可能会让玩家停在容易反复触发原版飞行检测的位置。网络问题也可能造成类似的连续踢出。一旦出现这种情况，玩家重连后可能立刻再次被踢出，通常需要管理员手动移动玩家，或通过服务器管理工具介入处理。

本 mod 的目的就是在飞行踢出真正执行前，先尝试把玩家移动到下方有效方块上，减少这类问题带来的人工处理成本。

## 功能

- 处理原版 `multiplayer.disconnect.flying` 踢出流程。
- 在踢出前，将玩家传送到当前 X/Z 下方最近的有效方块上方。
- 传送后清空速度并重置摔落距离。
- 支持普通玩家悬空踢出和载具悬空踢出。
- 可配置传送成功后是否继续踢出玩家。
- 如果玩家正下方找不到落点，可配置扩大为正方形半径范围搜索。
- 配置加载和重载时会在日志中输出当前配置状态。
- 在一个 jar 中同时提供 Fabric 和 NeoForge 支持，落点与 mixin 实现仍然只维护一份源码。

## 需求

- Minecraft：`1.21.1`
- 加载器：Fabric Loader 或 NeoForge
- Java：`21`
- 仅包含服务端逻辑，连接专用服务器的客户端不需要安装
- Fabric 不需要安装 Fabric API

## 配置

两个加载器使用相同的配置键和默认值。

NeoForge 会把服务端配置写到 `<world>/serverconfig/flightkickbypass-server.toml`：

```toml
kickAfterTeleport = true
expandedSearchRadius = 0
disconnectMessageSuffix = "Teleported to the nearest safe position before disconnecting."
teleportMessage = "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected."
```

Fabric 会写入 `config/flightkickbypass.json`：

```json
{
  "kickAfterTeleport": true,
  "expandedSearchRadius": 0,
  "disconnectMessageSuffix": "Teleported to the nearest safe position before disconnecting.",
  "teleportMessage": "Flying was detected for too long, so you were teleported to the nearest safe position instead of being disconnected."
}
```

`kickAfterTeleport` 选项说明：

- `true` 默认值：先把玩家传送到地面，然后继续执行原版踢出流程。
- `false`：如果传送成功，则取消本次踢出并重置悬空计数。如果玩家下方找不到有效方块，仍然执行原版踢出。

`expandedSearchRadius` 选项说明：

- `0` 默认值：只搜索玩家正下方。
- 大于 `0`：如果正下方搜索失败，则以玩家当前方块位置为中心搜索正方形范围。值为 `r` 时，搜索范围为 `(2r + 1) x (2r + 1)`。

向玩家展示的文本：

- `disconnectMessageSuffix`：当 `kickAfterTeleport` 为 `true` 且传送成功时，追加到飞行踢出原因的新一行文本。设为空字符串可以禁用这行额外提示。
- `teleportMessage`：当 `kickAfterTeleport` 为 `false` 且传送成功时，作为系统消息发送给玩家。设为空字符串可以禁用这条消息。

## 安装

1. 在服务器上安装 Minecraft 1.21.1 对应的 Fabric Loader 或 NeoForge。
2. 将同一个通用 mod jar 放入服务器的 `mods` 目录。
3. 启动一次服务器以生成配置文件。
4. 按需修改生成的配置。Fabric 修改后需要重启；NeoForge 也可以通过其支持的服务端配置重载机制应用。

## 构建

```sh
./gradlew build
```

构建产物会输出到：

```text
build/libs/flightkickbypass-1.21.1-fabric-neoforge-<version>.jar
```

## 说明

本 mod 不会给予玩家飞行权限，也不会修改原版移动检测本身。它只改变原版飞行踢出即将执行前的处理行为。

专用服务器只需在服务端安装。要在单人游戏的内部服务器中使用，需要把同一个 jar 安装到对应客户端实例。

项目图标为 AI 生成的说明性美术图，并非游戏内截图。
