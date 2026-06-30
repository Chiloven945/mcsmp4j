package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when a world upgrade fails.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param reason human-readable failure reason supplied by the server
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
