package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.IpBan;

import static java.util.Objects.requireNonNull;

/**
 * IP address was added to the IP ban list.
 *
 * <p>This event is decoded from the {@code minecraft:notification/ip_bans/added} notification. Register a listener
 * with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param ban the IP ban entry that was added
 */
public record IpBanAddedEvent(
        IpBan ban
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/ip_bans/added";

    /**
     * Validates that the notification payload is present.
     */
    public IpBanAddedEvent {
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
