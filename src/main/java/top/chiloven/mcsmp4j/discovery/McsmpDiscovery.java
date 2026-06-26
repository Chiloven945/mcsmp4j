package top.chiloven.mcsmp4j.discovery;

import java.util.concurrent.CompletableFuture;

/**
 * Access to the MCSMP JSON-RPC discovery endpoint.
 *
 * <p>Discovery calls {@code rpc.discover} and converts the server-provided schema into a
 * {@link McsmpCapabilities} snapshot. Use it when writing software that must support multiple Minecraft server versions
 * or optional protocol features.</p>
 */
@FunctionalInterface
public interface McsmpDiscovery {

    /**
     * Calls {@code rpc.discover} and converts the returned schema into mcsmp4j capabilities.
     *
     * @return a future containing the discovered capability snapshot
     */
    CompletableFuture<McsmpCapabilities> discover();

}
