package top.chiloven.mcsmp4j.protocol;

import tools.jackson.databind.JsonNode;

/**
 * Raw JSON-RPC notification received from the server.
 *
 * <p>A notification is a JSON-RPC message with a {@code method} and optional {@code params}, but no {@code id}. Because
 * it
 * has no id, the server does not expect a response from the client. Typed events in {@link top.chiloven.mcsmp4j.event}
 * are decoded from this raw representation.</p>
 *
 * @param method the full notification method name
 * @param params the raw parameter node; may be {@code null} or an empty array depending on what the server sent
 */
public record JsonRpcNotification(
        String method,
        JsonNode params
) {

}
