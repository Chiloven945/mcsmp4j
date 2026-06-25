package top.chiloven.mcsmp4j;

import top.chiloven.mcsmp4j.version.McsmpFeature;

/**
 * Thrown when a requested MCSMP feature is not advertised by the connected server.
 */
public final class McsmpUnsupportedFeatureException extends McsmpException {

    private final McsmpFeature feature;

    public McsmpUnsupportedFeatureException(McsmpFeature feature) {
        super("MCSMP feature is not supported by this server: " + feature);
        this.feature = feature;
    }

    public McsmpFeature feature() {
        return feature;
    }

}
