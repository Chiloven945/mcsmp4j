package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when a player is removed from the allowlist.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param player the player that was removed from the allowlist
 */
public record AllowlistRemovedEvent(
        Player player
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/allowlist/removed";

    /**
     * Validates that the notification payload is present.
     */
    public AllowlistRemovedEvent {
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
