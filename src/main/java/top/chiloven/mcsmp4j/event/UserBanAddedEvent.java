package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.UserBan;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when a user ban is added.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param ban the user-ban entry that was added; the protocol names this parameter {@code player}
 */
public record UserBanAddedEvent(
        UserBan ban
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/bans/added";

    /**
     * Validates that the notification payload is present.
     */
    public UserBanAddedEvent {
        requireNonNull(ban, "ban");
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
