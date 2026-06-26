package top.chiloven.mcsmp4j.event;

/**
 * Server save completed notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/saved} notification. This event indicates the
 * save operation has completed.</p>
 */
public record ServerSavedEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/saved";

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
