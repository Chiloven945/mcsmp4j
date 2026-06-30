package top.chiloven.mcsmp4j.event;

/**
 * Handle returned by typed or raw event listener registration.
 *
 * <p>Closing the subscription removes the associated listener from the registry. The method is idempotent, so cleanup
 * code
 * may safely close a subscription more than once. Closing the owning {@link top.chiloven.mcsmp4j.McsmpClient} also
 * closes its event registry and removes active listener registrations.</p>
 */
@FunctionalInterface
public interface EventSubscription extends AutoCloseable {

    /**
     * Unregisters the associated event listener.
     */
    @Override
    void close();

}
