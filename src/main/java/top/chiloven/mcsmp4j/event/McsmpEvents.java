package top.chiloven.mcsmp4j.event;

import java.util.function.Consumer;

/**
 * Registry for typed and raw MCSMP notification listeners.
 *
 * <p>Instances are obtained from {@link top.chiloven.mcsmp4j.McsmpClient#events()}. The registry converts raw JSON-RPC
 * notifications into typed {@link McsmpEvent} records when the notification is known, and it also exposes raw events
 * for unknown methods or extension namespaces.</p>
 *
 * <h2>Listener registration</h2>
 *
 * <p>{@link #on(Class, java.util.function.Consumer)} registers a listener for one event class or any subclass
 * assignable
 * to that class. {@link #onRaw(java.util.function.Consumer)} receives all raw events.
 * {@link #onRaw(String, java.util.function.Consumer)} receives only one exact method name.</p>
 *
 * <h2>Callback behavior</h2>
 *
 * <p>Callbacks are invoked from the client's receive path. Keep listeners fast, avoid blocking calls, and dispatch to
 * an
 * application executor for expensive work. Closing the returned {@link EventSubscription} unregisters the
 * listener.</p>
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
