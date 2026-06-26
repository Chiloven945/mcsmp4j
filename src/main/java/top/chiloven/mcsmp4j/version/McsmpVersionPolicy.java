package top.chiloven.mcsmp4j.version;

/**
 * Controls how aggressively mcsmp4j enables compatibility behavior around protocol-version changes.
 *
 * <p>The policy affects local client behavior only. It does not change what the server supports, and it does not
 * emulate unsupported methods. Use discovery and {@link McsmpFeature} checks for server-side capability decisions.</p>
 */
public enum McsmpVersionPolicy {

    /**
     * Prefer current protocol names and do not enable legacy aliases unless explicitly configured elsewhere.
     */
    STRICT,

    /**
     * Enable safe local compatibility aliases, such as the legacy notification prefix normalization.
     */
    COMPATIBLE

}
