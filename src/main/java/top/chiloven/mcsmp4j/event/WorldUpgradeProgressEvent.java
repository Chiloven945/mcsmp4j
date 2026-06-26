package top.chiloven.mcsmp4j.event;

/**
 * World upgrade progress changed.
 *
 * <p>This event is decoded from the upcoming {@code minecraft:notification/world/upgrade_progress} notification.
 * The protocol reports progress as a number between {@code 0.0} and {@code 1.0}, inclusive.</p>
 *
 * @param progress fractional progress of the world upgrade, where {@code 0.0} means not started and {@code 1.0} means
 *                 complete
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
