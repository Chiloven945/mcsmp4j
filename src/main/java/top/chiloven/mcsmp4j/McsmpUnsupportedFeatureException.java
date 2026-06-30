package top.chiloven.mcsmp4j;

import top.chiloven.mcsmp4j.version.McsmpFeature;

import static java.util.Objects.requireNonNull;

/**
 * Exception raised when application code attempts to require a capability that the discovered server does not expose.
 *
 * <p>This type is intended for code paths that explicitly combine
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities}
 * with {@link top.chiloven.mcsmp4j.version.McsmpFeature}. The typed API itself remains permissive because custom
 * servers may support features in non-standard combinations. Applications can use this exception to produce clear
 * messages such as "world upgrade notifications require protocol 3.1.0 or a server that advertises that
 * notification".</p>
 */
public final class McsmpUnsupportedFeatureException extends McsmpException {

    /**
     * Missing feature that caused this exception.
     */
    private final McsmpFeature feature;

    /**
     * Creates an exception for the missing feature.
     *
     * @param feature the feature that was required but not supported by the connected server
     */
    public McsmpUnsupportedFeatureException(McsmpFeature feature) {
        super("MCSMP feature is not supported by the connected server: " + feature);
        this.feature = requireNonNull(feature, "feature");
    }

    /**
     * Returns the missing feature.
     *
     * @return the feature that caused this exception
     */
    public McsmpFeature feature() {
        return feature;
    }

}
