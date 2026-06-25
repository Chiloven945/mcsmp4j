package top.chiloven.mcsmp4j.protocol;

import tools.jackson.databind.JsonNode;

/**
 * Raw JSON-RPC notification sent by the server.
 */
public record JsonRpcNotification(
        String method,
        JsonNode params
) {

}
