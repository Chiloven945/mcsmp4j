package top.chiloven.mcsmp4j.internal;

import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotificationListener;
import top.chiloven.mcsmp4j.protocol.JsonRpcSubscription;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.requireNonNull;

final class NotificationDispatcher {

    private final List<JsonRpcNotificationListener> listeners = new CopyOnWriteArrayList<>();

    JsonRpcSubscription subscribe(JsonRpcNotificationListener listener) {
        requireNonNull(listener, "listener");
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    void dispatch(JsonRpcNotification notification) {
        for (var listener : listeners) {
            try {
                listener.onNotification(notification);
            } catch (RuntimeException ignored) {
                // Listener failures must not break the underlying MCSMP session.
            }
        }
    }

}
