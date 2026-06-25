package top.chiloven.mcsmp4j.protocol;

/**
 * Listener for raw JSON-RPC notifications.
 */
@FunctionalInterface
public interface JsonRpcNotificationListener {

    void onNotification(JsonRpcNotification notification);

}
