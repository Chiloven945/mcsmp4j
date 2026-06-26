package top.chiloven.mcsmp4j.version;

/**
 * Feature flags inferred from {@code rpc.discover} and the known MCSMP protocol history.
 *
 * <p>Features provide a stable Java-level vocabulary for optional protocol behavior. Use
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities#supports(McsmpFeature)} or
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities#require(McsmpFeature)} before depending on behavior that is
 * not present in every protocol version.</p>
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
