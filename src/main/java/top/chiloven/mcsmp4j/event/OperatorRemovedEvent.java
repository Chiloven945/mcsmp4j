package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Operator;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when a player is removed from the operator list.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param operator the operator entry that was removed as reported by the server
 */
public record OperatorRemovedEvent(
        Operator operator
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/operators/removed";

    /**
     * Validates that the notification payload is present.
     */
    public OperatorRemovedEvent {
        requireNonNull(operator, "operator");
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
