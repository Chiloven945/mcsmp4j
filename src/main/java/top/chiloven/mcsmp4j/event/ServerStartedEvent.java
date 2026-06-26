package top.chiloven.mcsmp4j.event;

/**
 * Server startup completed notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/started} notification. On newer protocol
 * versions the management endpoint may be available before this event is emitted.</p>
 */
public record ServerStartedEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/started";

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
