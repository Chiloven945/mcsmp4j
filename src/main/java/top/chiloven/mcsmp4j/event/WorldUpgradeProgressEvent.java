package top.chiloven.mcsmp4j.event;

/**
 * Progress event for a running world upgrade.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param progress progress as a number from {@code 0.0} to {@code 1.0}
 */
public record WorldUpgradeProgressEvent(
        double progress
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/world/upgrade_progress";

    /**
     * Validates that progress is within the protocol-defined range from 0 to 1.
     */
    public WorldUpgradeProgressEvent {
        if (progress < 0.0d || progress > 1.0d) {
            throw new IllegalArgumentException("progress must be between 0 and 1");
        }
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
