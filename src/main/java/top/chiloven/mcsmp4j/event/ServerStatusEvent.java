package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.ServerState;

import static java.util.Objects.requireNonNull;

/**
 * Server status heartbeat notification.
 *
 * <p>This event is decoded from the {@code minecraft:notification/server/status} notification. Register a listener
 * with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)} to receive this typed event.</p>
 *
 * @param status the server status snapshot carried by the heartbeat
 */
public record ServerStatusEvent(
        ServerState status
) implements McsmpEvent {

    /**
     * The JSON-RPC notification method decoded into this event type.
     */
    public static final String METHOD = "minecraft:notification/server/status";

    /**
     * Validates that the notification payload is present.
     */
    public ServerStatusEvent {
        requireNonNull(status, "status");
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
