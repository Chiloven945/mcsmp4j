package top.chiloven.mcsmp4j.version;

/**
 * How aggressively mcsmp4j should enable compatibility behavior around protocol-version changes.
 */
public enum McsmpVersionPolicy {

    /**
     * Prefer current protocol names and do not enable legacy aliases unless explicitly requested.
     */
    STRICT,

    /**
     * Enable compatibility aliases that are safe to dispatch locally, such as the legacy notification prefix.
     */
    COMPATIBLE

}
