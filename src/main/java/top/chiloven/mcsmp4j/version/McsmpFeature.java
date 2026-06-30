package top.chiloven.mcsmp4j.version;

/**
 * Named feature gates derived from MCSMP protocol history and discovery output.
 *
 * <p>A feature is a semantic capability rather than a raw method name. For example, {@link #TYPED_GAMERULE_VALUE}
 * represents the protocol change where game-rule values became JSON booleans/integers instead of strings; it may be
 * inferred from protocol version, schema shape, or discovered methods depending on server output.</p>
 *
 * <p>Use features when writing code that must explain why an operation is unavailable. For exact protocol
 * introspection, use {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities#supportsMethod(String)} and
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities#supportsNotification(String)}.</p>
 */
public enum McsmpFeature {

    /**
     * Client authentication is required by the server.
     */
    AUTHENTICATION,

    /**
     * TLS is expected to be enabled by default for the management endpoint.
     */
    TLS_BY_DEFAULT,

    /**
     * Notifications use the modern {@code minecraft:notification/} prefix.
     */
    MINECRAFT_NOTIFICATION_PREFIX,

    /**
     * The {@code minecraft:notification/server/activity} notification is available.
     */
    SERVER_ACTIVITY_NOTIFICATION,

    /**
     * The server supports or requires origin allowlist checks during the WebSocket handshake.
     */
    ORIGIN_ALLOWLIST,

    /**
     * Game-rule values are represented as typed booleans or integers rather than strings.
     */
    TYPED_GAMERULE_VALUE,

    /**
     * Management discovery and status can be available before the dedicated server has fully started.
     */
    PRE_START_DISCOVERY,

    /**
     * World-upgrade notifications are available.
     */
    WORLD_UPGRADE_NOTIFICATIONS

}
