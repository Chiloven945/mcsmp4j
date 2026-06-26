# mcsmp4j

`mcsmp4j` is a Java client library for the Minecraft Server Management Protocol (MCSMP): JSON-RPC 2.0 over WebSocket
for dedicated servers.

The library provides three layers:

1. Strongly typed Java APIs for the official `minecraft:*` method groups.
2. Typed notification events for official `minecraft:notification/*` messages.
3. A raw JSON-RPC API for custom namespaces and future protocol additions.

## Requirements

- Java 21+
- A Minecraft dedicated server with the management server enabled
- Gradle 9.x for building this project

## Install from a Maven repository

```kotlin
dependencies {
    implementation("top.chiloven:mcsmp4j:0.1.0-alpha.1")
}
```

This project is configured for Maven Central publication. To create the local Maven Central staging repository for verification:

```bash
./gradlew clean stageMavenCentral
```

The unsigned staging artifacts are written to `build/staging-deploy`. Final signing, checksum generation, Maven Central rule validation, and upload are handled by JReleaser during `releaseMavenCentral`.

## Server configuration

The management API is disabled by default. A typical `server.properties` setup looks like this:

```properties
management-server-enabled=true
management-server-host=localhost
management-server-port=25585
management-server-secret=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn
management-server-allowed-origins=mcsmp4j
management-server-tls-enabled=true
```

Use `wss://` when TLS is enabled and `ws://` when `management-server-tls-enabled=false`.

## Connect

```java
try (var client = McsmpClient.builder()
        .endpoint(URI.create("wss://127.0.0.1:25585"))
        .secret("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn")
        .origin("mcsmp4j")
        .build()) {

    client.connect().join();

    var status = client.server().status().join();
    System.out.println(status.version().name());
}
```

`secret(...)` uses the recommended `Authorization: Bearer <secret>` handshake header. Browser-like clients can use the
WebSocket subprotocol authentication style instead:

```java
var client = McsmpClient.builder()
        .endpoint(URI.create("wss://127.0.0.1:25585"))
        .auth(McsmpAuth.websocketSubprotocol("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn"))
        .origin("mcsmp4j")
        .build();
```

For pre-authentication snapshots or tests, use `McsmpAuth.none()` explicitly.

## Core APIs

```java
List<Player> online = client.players().list().join();
client.players().kick(KickPlayer.withMessage(Player.byName("Steve"), Message.literal("Bye"))).join();

client.allowlist().add(Player.byName("Alex")).join();
client.bans().add(UserBan.byName("Griefer42")).join();
client.ipBans().add(IncomingIpBan.ip("203.0.113.8")).join();
client.operators().add(Operator.of(Player.byName("Admin"))).join();

client.server().save(true).join();
client.server().systemMessage(SystemMessage.chat(Message.literal("Maintenance soon"))).join();
```

## Server settings and gamerules

```java
client.serverSettings().setDifficulty(Difficulty.HARD).join();
client.serverSettings().setMaxPlayers(20).join();
client.serverSettings().setMotd("Welcome to the server").join();

client.gamerules().update(UntypedGameRule.bool("doDaylightCycle", false)).join();
client.gamerules().update(UntypedGameRule.integer("randomTickSpeed", 3)).join();
```

Modern servers return boolean or integer gamerule values. Older snapshots may return strings; `GameRuleValue` accepts
all three shapes so applications can remain compatible.

## Notifications

```java
var subscription = client.events().on(PlayerJoinedEvent.class, event -> {
    System.out.println(event.player().name() + " joined");
});

client.events().on(ServerStatusEvent.class, event -> {
    System.out.println("Online: " + event.status().onlinePlayerCount());
});
```

Unknown official or third-party notifications can be handled as raw events:

```java
client.events().onRaw("my_mod:notification/reloaded", event -> {
    System.out.println(event.params());
});
```

By default, `McsmpVersionPolicy.COMPATIBLE` is enabled and accepts the legacy `notification:*` prefix as an alias for
`minecraft:notification/*`. Set `versionPolicy(McsmpVersionPolicy.STRICT)` to disable this unless
`legacyNotificationPrefix(true)` is explicitly set.

## Discovery and feature checks

```java
McsmpCapabilities capabilities = client.discover().join();

if (capabilities.supports(McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS)) {
    client.events().on(WorldUpgradeProgressEvent.class, event -> {
        System.out.println("World upgrade: " + event.progress());
    });
}

if (capabilities.supportsMethod("minecraft:gamerules/update")) {
    client.gamerules().update(UntypedGameRule.bool("keepInventory", true)).join();
}
```

`client.capabilities()` returns the most recent cached discovery result after `discover()` has completed.

## Raw JSON-RPC

Use the raw API for extension namespaces or protocol features that are newer than the typed facade:

```java
JsonNode result = client.raw()
        .callJson("my_mod:maintenance/reload")
        .join();
```

Typed conversion is also available:

```java
MyResult result = client.raw()
        .call("my_mod:status", MyResult.class)
        .join();
```

## TLS and self-signed certificates

Production code should use a normal JVM trust store or a certificate signed by a trusted CA. For local development with
a self-signed certificate, provide an `SSLContext` that trusts the server certificate:

```java
var client = McsmpClient.builder()
        .endpoint(URI.create("wss://127.0.0.1:25585"))
        .secret(secret)
        .origin("mcsmp4j")
        .sslContext(localDevelopmentSslContext)
        .build();
```

Avoid disabling certificate validation globally in production applications.

## Protocol compatibility table

| Protocol stage                       | Library handling                                                              |
|--------------------------------------|-------------------------------------------------------------------------------|
| Authentication and TLS               | Bearer, WebSocket subprotocol auth, custom headers, custom `SSLContext`       |
| Notification prefix change           | Modern `minecraft:notification/*` plus optional legacy `notification:*` alias |
| `1.1.0` server activity notification | `ServerActivityEvent` and capability inference                                |
| `2.0.0` typed gamerule values        | `GameRuleValue` supports boolean, integer, and legacy string                  |
| `3.0.0` pre-start discovery/status   | `rpc.discover`, `ServerState.started`, feature gate support                   |
| `3.1.0` world upgrade notifications  | typed world upgrade events and `WORLD_UPGRADE_NOTIFICATIONS` feature          |

## Error handling

All library runtime failures extend `McsmpException`:

- `McsmpConnectionException` for WebSocket transport failures
- `McsmpAuthenticationException` for HTTP 401 handshake rejection
- `McsmpTimeoutException` for request timeout
- `McsmpProtocolException` for malformed JSON-RPC or incompatible payloads
- `McsmpRemoteException` for JSON-RPC remote error objects
- `McsmpUnsupportedFeatureException` for missing capability gates

## Build and test

```bash
./gradlew test
```

The test suite includes a minimal in-process mock WebSocket server for JSON-RPC round trips, fragmented responses,
notification dispatch, and 401 authentication rejection mapping.
