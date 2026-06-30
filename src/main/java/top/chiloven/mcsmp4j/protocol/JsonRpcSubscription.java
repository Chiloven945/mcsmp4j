package top.chiloven.mcsmp4j.protocol;

/**
 * Handle returned by a raw JSON-RPC notification registration.
 *
 * <p>Closing the subscription unregisters the associated listener. The operation is idempotent; callers may close a
 * subscription from cleanup code even if it may already have been closed because the client itself is closing.</p>
 */
@FunctionalInterface
public interface JsonRpcSubscription extends AutoCloseable {

    /**
     * Unregisters the associated raw notification listener.
     */
    @Override
    void close();

}
