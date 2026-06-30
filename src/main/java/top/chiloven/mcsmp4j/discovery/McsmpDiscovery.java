package top.chiloven.mcsmp4j.discovery;

import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for calling {@code rpc.discover} and receiving parsed capabilities.
 *
 * <p>Discovery is the bridge between the raw schema returned by a server and the application-friendly
 * {@link McsmpCapabilities} model. Implementations call the server's JSON-RPC discovery method, parse advertised
 * methods, notifications, and version information, and return an immutable snapshot.</p>
 *
 * <p>Applications should call discovery shortly after connection when they support more than one server version or when
 * they
 * need optional features such as typed game-rule values, pre-start status, activity notifications, or world-upgrade
 * notifications. Discovery is not mandatory for simple tools that know exactly which server version they target.</p>
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
