package top.chiloven.mcsmp4j.protocol;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * JSON-RPC 2.0 error payload.
 */
public record JsonRpcError(
        int code,
        String message,
        @Nullable JsonNode data
) {

}
