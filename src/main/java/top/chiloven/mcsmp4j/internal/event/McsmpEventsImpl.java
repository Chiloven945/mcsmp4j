package top.chiloven.mcsmp4j.internal.event;

import org.jspecify.annotations.Nullable;
import top.chiloven.mcsmp4j.event.EventSubscription;
import top.chiloven.mcsmp4j.event.McsmpEvent;
import top.chiloven.mcsmp4j.event.McsmpEvents;
import top.chiloven.mcsmp4j.event.RawMcsmpEvent;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotificationListener;
import top.chiloven.mcsmp4j.protocol.JsonRpcSubscription;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class McsmpEventsImpl implements McsmpEvents, AutoCloseable {

    private final McsmpEventDecoder decoder;
    private final JsonRpcSubscription rawSubscription;
    private final List<TypedRegistration<?>> typedRegistrations = new CopyOnWriteArrayList<>();
    private final List<RawRegistration> rawRegistrations = new CopyOnWriteArrayList<>();

    public McsmpEventsImpl(
            McsmpEventDecoder decoder,
            Function<JsonRpcNotificationListener, JsonRpcSubscription> subscribe
    ) {
        this.decoder = requireNonNull(decoder, "decoder");
        this.rawSubscription = requireNonNull(subscribe, "subscribe").apply(this::dispatch);
    }

    public void dispatch(JsonRpcNotification notification) {
        var raw = new RawMcsmpEvent(notification.method(), notification.params());
        dispatchRaw(raw);
        decoder.decode(notification).ifPresent(this::dispatchTyped);
    }

    private void dispatchRaw(RawMcsmpEvent event) {
        rawRegistrations.stream()
                .filter(reg ->
                        reg.method() == null || reg.method().equals(event.method())
                )
                .<Runnable>map(reg ->
                        () -> reg.listener().accept(event)
                )
                .forEach(McsmpEventsImpl::safely);
    }

    private void dispatchTyped(McsmpEvent event) {
        typedRegistrations.stream()
                .filter(reg -> reg.eventType().isInstance(event))
                .forEach(reg -> reg.dispatch(event));
    }

    private static void safely(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException ignored) {
            // Listener failures must not break the underlying MCSMP session.
        }
    }

    @Override
    public <E extends McsmpEvent> EventSubscription on(
            Class<E> eventType,
            Consumer<? super E> listener
    ) {
        requireNonNull(eventType, "eventType");
        requireNonNull(listener, "listener");
        var registration = new TypedRegistration<>(eventType, listener);
        typedRegistrations.add(registration);
        return () -> typedRegistrations.remove(registration);
    }

    @Override
    public EventSubscription onRaw(Consumer<? super RawMcsmpEvent> listener) {
        requireNonNull(listener, "listener");
        var registration = new RawRegistration(null, listener);
        rawRegistrations.add(registration);
        return () -> rawRegistrations.remove(registration);
    }

    @Override
    public EventSubscription onRaw(
            String method,
            Consumer<? super RawMcsmpEvent> listener
    ) {
        requireNonNull(method, "method");
        requireNonNull(listener, "listener");
        if (method.isBlank()) {
            throw new IllegalArgumentException("method must not be blank");
        }
        var registration = new RawRegistration(method, listener);
        rawRegistrations.add(registration);
        return () -> rawRegistrations.remove(registration);
    }

    @Override
    public void close() {
        rawSubscription.close();
        typedRegistrations.clear();
        rawRegistrations.clear();
    }

    private record TypedRegistration<E extends McsmpEvent>(
            Class<E> eventType,
            Consumer<? super E> listener
    ) {

        private void dispatch(McsmpEvent event) {
            safely(() -> listener.accept(eventType.cast(event)));
        }

    }

    private record RawRegistration(
            @Nullable String method,
            Consumer<? super RawMcsmpEvent> listener
    ) {

    }

}
