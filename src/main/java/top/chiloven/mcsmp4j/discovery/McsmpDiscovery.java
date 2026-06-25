package top.chiloven.mcsmp4j.discovery;

import java.util.concurrent.CompletableFuture;

/**
 * Access to the JSON-RPC discovery endpoint.
 */
@FunctionalInterface
public interface McsmpDiscovery {

    /**
     * Calls {@code rpc.discover} and converts the returned schema into mcsmp4j capabilities.
     */
    CompletableFuture<McsmpCapabilities> discover();

}
