package top.chiloven.mcsmp4j.event;

import java.util.function.Consumer;

/**
 * Type-safe MCSMP notification listener registry.
 */
public interface McsmpEvents {

    <E extends McsmpEvent> EventSubscription on(
            Class<E> eventType,
            Consumer<? super E> listener
    );

    EventSubscription onRaw(
            Consumer<? super RawMcsmpEvent> listener
    );

    EventSubscription onRaw(
            String method,
            Consumer<? super RawMcsmpEvent> listener
    );

}
