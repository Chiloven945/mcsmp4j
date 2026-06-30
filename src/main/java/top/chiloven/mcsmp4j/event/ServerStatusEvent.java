package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.ServerState;

import static java.util.Objects.requireNonNull;

/**
 * Heartbeat event containing the current server status snapshot.
 *
 * <p>This event is created from a JSON-RPC notification received over the active WebSocket. It is delivered only to
 * listeners registered before the notification is processed and is not stored for replay. Use the corresponding typed
 * API to read an initial snapshot before subscribing when your application maintains local state.</p>
 *
 * @param status the server state snapshot carried by the heartbeat
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
