package top.chiloven.mcsmp4j.event;

/**
 * Server shutdown started notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/stopping} notification. This event is emitted
 * when the server begins stopping.</p>
 */
public record ServerStoppingEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/stopping";

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
