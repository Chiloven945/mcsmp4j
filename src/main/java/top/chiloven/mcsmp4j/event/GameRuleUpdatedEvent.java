package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.TypedGameRule;

import static java.util.Objects.requireNonNull;

/**
 * A game rule value was changed.
 *
 * <p>This event is decoded from the {@code minecraft:notification/gamerules/updated} notification. Register a listener
 * with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param gamerule the updated game rule, including its key, type, and new value
 */
public record GameRuleUpdatedEvent(
        TypedGameRule gamerule
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/gamerules/updated";

    /**
     * Validates that the notification payload is present.
     */
    public GameRuleUpdatedEvent {
        requireNonNull(gamerule, "gamerule");
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
