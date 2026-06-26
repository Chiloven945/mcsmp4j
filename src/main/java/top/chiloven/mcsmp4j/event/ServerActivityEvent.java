package top.chiloven.mcsmp4j.event;

/**
 * Network connection initialized activity notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/activity} notification. This event is
 * available only on protocol versions that include the server activity notification.</p>
 */
public record ServerActivityEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/activity";

    /**
     * Returns the JSON-RPC notification method name for this event type.
     *
     * @return {@link #METHOD}
     */
    @Override
    public String method() {
        return METHOD;
    }

}
