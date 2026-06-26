package top.chiloven.mcsmp4j.event;

import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Raw server notification event.
 *
 * <p>This event is used when the notification is not decoded into a more specific record, and by raw event
 * listeners that want access to the original method and parameter JSON. It is also useful for custom modded
 * namespaces.</p>
 *
 * @param method the full JSON-RPC notification method name
 * @param params the raw notification parameters as a JSON node
 */
public record RawMcsmpEvent(
        String method,
        JsonNode params
) implements McsmpEvent {

    /**
     * Validates that both method and parameter JSON are present.
     */
    public RawMcsmpEvent {
        requireNonNull(method, "method");
        requireNonNull(params, "params");
    }

}
