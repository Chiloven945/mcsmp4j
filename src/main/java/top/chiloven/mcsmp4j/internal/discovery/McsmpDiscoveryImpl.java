package top.chiloven.mcsmp4j.internal.discovery;

import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.discovery.McsmpCapabilities;
import top.chiloven.mcsmp4j.discovery.McsmpDiscovery;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class McsmpDiscoveryImpl implements McsmpDiscovery {

    private static final String DISCOVER_METHOD = "rpc.discover";

    private final RawApi raw;

    public McsmpDiscoveryImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    public static McsmpCapabilities parse(JsonNode schema) {
        return McsmpCapabilitiesParser.parse(schema);
    }

    @Override
    public CompletableFuture<McsmpCapabilities> discover() {
        return raw.callJson(DISCOVER_METHOD).thenApply(McsmpCapabilitiesParser::parse);
    }

}
