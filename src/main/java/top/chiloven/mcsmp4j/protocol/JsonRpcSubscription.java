package top.chiloven.mcsmp4j.protocol;

/**
 * Handle returned when registering a notification listener.
 */
@FunctionalInterface
public interface JsonRpcSubscription extends AutoCloseable {

    @Override
    void close();

}
