package top.chiloven.mcsmp4j.event;

/**
 * Handle returned when registering an MCSMP event listener.
 */
@FunctionalInterface
public interface EventSubscription extends AutoCloseable {

    @Override
    void close();

}
