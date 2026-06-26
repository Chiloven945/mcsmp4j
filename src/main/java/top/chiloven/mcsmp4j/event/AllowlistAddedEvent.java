package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

/**
 * Player was added to the server allowlist.
 *
 * <p>This event is decoded from the {@code minecraft:notification/allowlist/added} notification. Register a listener
 * with {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param player the player that was added to the allowlist
 */
public record AllowlistAddedEvent(
        Player player
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/allowlist/added";

    /**
     * Validates that the notification payload is present.
     */
    public AllowlistAddedEvent {
        requireNonNull(player, "player");
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
