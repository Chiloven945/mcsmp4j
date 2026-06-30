package top.chiloven.mcsmp4j.version;

/**
 * High-level compatibility policy used by client configuration and event decoding.
 *
 * <p>The policy controls how much compatibility behavior mcsmp4j should enable locally when protocol history contains
 * transitional shapes. For example, {@link #COMPATIBLE} accepts the legacy notification prefix while still using the
 * modern typed event model. The policy does not force servers to support methods and does not hide remote errors; it
 * only controls client-side interpretation where the library can safely support more than one shape.</p>
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
