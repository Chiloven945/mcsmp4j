package top.chiloven.mcsmp4j.version;

/**
 * Feature flags inferred from {@code rpc.discover} and the known protocol history.
 */
public enum McsmpFeature {

    AUTHENTICATION,
    TLS_BY_DEFAULT,
    MINECRAFT_NOTIFICATION_PREFIX,
    SERVER_ACTIVITY_NOTIFICATION,
    ORIGIN_ALLOWLIST,
    TYPED_GAMERULE_VALUE,
    PRE_START_DISCOVERY,
    WORLD_UPGRADE_NOTIFICATIONS

}
