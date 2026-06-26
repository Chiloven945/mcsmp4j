package top.chiloven.mcsmp4j.event;

/**
 * Server save started notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/saving} notification. This event indicates
 * that the server has started saving state.</p>
 */
public record ServerSavingEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/saving";

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
