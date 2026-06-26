package top.chiloven.mcsmp4j.event;

import java.util.function.Consumer;

/**
 * Type-safe MCSMP notification listener registry.
 *
 * <p>Instances are obtained from {@link top.chiloven.mcsmp4j.McsmpClient#events()}. Listeners are invoked by the
 * client's WebSocket receive path whenever the server sends a JSON-RPC notification. Listener callbacks should return
 * quickly and offload expensive work to an application executor if necessary.</p>
 */
public interface McsmpEvents {

    /**
     * Registers a listener for a specific typed event class.
     *
     * <p>The listener is called only for events that are assignable to {@code eventType}. Close the returned
     * subscription to unregister the listener.</p>
     *
     * @param eventType the event class to receive
     * @param listener  the callback invoked for matching events
     * @param <E>       the event type handled by the listener
     *
     * @return a subscription handle that unregisters the listener when closed
     */
    <E extends McsmpEvent> EventSubscription on(
            Class<E> eventType,
            Consumer<? super E> listener
    );

    /**
     * Registers a listener for all raw notifications, including extension namespaces and unknown official events.
     *
     * @param listener the callback invoked for every raw notification
     *
     * @return a subscription handle that unregisters the listener when closed
     */
    EventSubscription onRaw(
            Consumer<? super RawMcsmpEvent> listener
    );

    /**
     * Registers a listener for raw notifications with one exact method name.
     *
     * @param method   the full notification method name to receive
     * @param listener the callback invoked for matching raw notifications
     *
     * @return a subscription handle that unregisters the listener when closed
     */
    EventSubscription onRaw(
            String method,
            Consumer<? super RawMcsmpEvent> listener
    );

}
