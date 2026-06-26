package top.chiloven.mcsmp4j.protocol;

import tools.jackson.databind.JsonNode;

/**
 * Raw JSON-RPC notification sent by the server.
 *
 * <p>A notification is a JSON-RPC message that has a {@code method} and parameters but no {@code id}. It cannot
 * be answered and is used by MCSMP for server events such as player joins, saves, and status heartbeats.</p>
 *
 * @param method the full notification method name
 * @param params the raw notification parameters as a JSON node
 */
public record JsonRpcNotification(
        String method,
        JsonNode params
) {

}
