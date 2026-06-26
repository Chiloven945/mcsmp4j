package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

/**
 * IP address was removed from the IP ban list.
 *
 * <p>This event is decoded from the {@code minecraft:notification/ip_bans/removed} notification. The protocol
 * carries only the removed address string rather than the full former ban entry.</p>
 *
 * @param ip the IP address that was removed from the ban list
 */
public record IpBanRemovedEvent(
        String ip
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/ip_bans/removed";

    /**
     * Validates that the notification payload is present.
     */
    public IpBanRemovedEvent {
        requireNonNull(ip, "ip");
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
