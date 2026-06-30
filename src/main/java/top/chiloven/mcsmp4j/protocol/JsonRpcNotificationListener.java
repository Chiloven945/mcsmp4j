package top.chiloven.mcsmp4j.protocol;

/**
 * Callback for receiving raw JSON-RPC notifications.
 *
 * <p>Register a listener with {@link top.chiloven.mcsmp4j.McsmpClient#onNotification(JsonRpcNotificationListener)}
 * when
 * an application needs every notification exactly as it arrived, including extension namespaces and unknown official
 * messages. Typed event listeners are usually more convenient when a stable Java event class exists.</p>
 *
 * <p>Listener implementations should return quickly. Blocking in this callback can delay processing of later WebSocket
 * messages and therefore delay responses to pending requests.</p>
 */
@FunctionalInterface
public interface JsonRpcNotificationListener {

    /**
     * Handles a raw JSON-RPC notification.
     *
     * @param notification the notification received from the server
     */
    void onNotification(JsonRpcNotification notification);

}
