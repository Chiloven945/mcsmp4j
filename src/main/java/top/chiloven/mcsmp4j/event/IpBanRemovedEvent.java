package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

/**
 * Event emitted when an IP ban is removed.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param ip the IP address string removed from the ban list; the protocol names this parameter {@code player}
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
