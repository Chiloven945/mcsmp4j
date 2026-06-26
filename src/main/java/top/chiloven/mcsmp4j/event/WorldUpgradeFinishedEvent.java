package top.chiloven.mcsmp4j.event;

/**
 * World upgrade finished notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/world/upgrade_finished} notification. This upcoming
 * notification is emitted by servers that advertise world-upgrade notifications.</p>
 */
public record WorldUpgradeFinishedEvent() implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/world/upgrade_finished";

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
