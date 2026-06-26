package top.chiloven.mcsmp4j.event;

/**
 * Handle returned when registering an MCSMP event listener.
 *
 * <p>Subscriptions are lightweight and idempotent from the caller's perspective: calling {@link #close()} more
 * than once should be harmless. Applications should close subscriptions when a UI component, plugin, or service no
 * longer needs notifications to avoid retaining listener references.</p>
 */
@FunctionalInterface
public interface EventSubscription extends AutoCloseable {

    /**
     * Unregisters the associated event listener.
     */
    @Override
    void close();

}
