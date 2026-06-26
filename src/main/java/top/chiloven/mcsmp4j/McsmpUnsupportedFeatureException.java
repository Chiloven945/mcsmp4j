package top.chiloven.mcsmp4j;

import top.chiloven.mcsmp4j.version.McsmpFeature;

import static java.util.Objects.requireNonNull;

/**
 * Raised when user code requires an optional protocol feature that the connected server does not advertise.
 *
 * <p>The feature set is normally produced by {@link top.chiloven.mcsmp4j.McsmpClient#discover()} and represented
 * by {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities}. This exception is useful when a library or application
 * wants to fail fast before using functionality introduced by newer protocol versions.</p>
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
