package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Operator;

import static java.util.Objects.requireNonNull;

/**
 * Player was removed from the operator list.
 *
 * <p>This event is decoded from the {@code minecraft:notification/operators/removed} notification. Register a listener
 * with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param operator the operator entry that was removed
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
