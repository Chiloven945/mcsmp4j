package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

/**
 * Player was removed from the user ban list.
 *
 * <p>This event is decoded from the {@code minecraft:notification/bans/removed} notification. Register a listener with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param player the player whose ban entry was removed
 */
public record UserBanRemovedEvent(
        Player player
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/bans/removed";

    /**
     * Validates that the notification payload is present.
     */
    public UserBanRemovedEvent {
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
