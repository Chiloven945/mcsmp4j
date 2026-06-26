package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.UserBan;

import static java.util.Objects.requireNonNull;

/**
 * Player was added to the user ban list.
 *
 * <p>This event is decoded from the {@code minecraft:notification/bans/added} notification. Register a listener with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param ban the user ban entry that was added
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
