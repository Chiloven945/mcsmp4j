package top.chiloven.mcsmp4j.protocol;

/**
 * Handle returned when registering a raw JSON-RPC notification listener.
 *
 * <p>Closing the subscription unregisters the listener and allows it to be garbage-collected. Subscriptions are
 * intended to be used with try-with-resources or stored alongside the component that owns the listener.</p>
 */
@FunctionalInterface
public interface JsonRpcSubscription extends AutoCloseable {

    /**
     * Unregisters the associated raw notification listener.
     */
    @Override
    void close();

}
