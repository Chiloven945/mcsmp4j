package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.TypedGameRule;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when a game rule is updated.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param gamerule the updated typed game-rule state
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
