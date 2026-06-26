package top.chiloven.mcsmp4j.protocol;

/**
 * Listener for raw JSON-RPC notifications.
 *
 * <p>Register this listener through
 * {@link top.chiloven.mcsmp4j.McsmpClient#onNotification(JsonRpcNotificationListener)}
 * when an application needs access to raw notification messages before or instead of typed event decoding.</p>
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
