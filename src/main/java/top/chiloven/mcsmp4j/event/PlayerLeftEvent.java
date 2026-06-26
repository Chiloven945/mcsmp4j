package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

/**
 * Player left the server.
 *
 * <p>This event is decoded from the {@code minecraft:notification/players/left} notification. Register a listener with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param player the player that left
 */
public record PlayerLeftEvent(
        Player player
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/players/left";

    /**
     * Validates that the notification payload is present.
     */
    public PlayerLeftEvent {
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
