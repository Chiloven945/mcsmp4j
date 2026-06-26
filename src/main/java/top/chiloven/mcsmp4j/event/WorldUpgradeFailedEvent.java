package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

/**
 * World upgrade failed.
 *
 * <p>This event is decoded from the upcoming {@code minecraft:notification/world/upgrade_failed} notification.
 * It is only emitted by protocol versions that advertise world-upgrade notifications.</p>
 *
 * @param reason the server-provided failure reason
 */
public record WorldUpgradeFailedEvent(
        String reason
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/world/upgrade_failed";

    /**
     * Validates that the failure reason is present.
     */
    public WorldUpgradeFailedEvent {
        requireNonNull(reason, "reason");
    }

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
