package top.chiloven.mcsmp4j.protocol;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * Raw JSON-RPC error object returned by a server.
 *
 * <p>This record mirrors the standard JSON-RPC {@code error} object: a numeric code, a message, and optional
 * structured
 * data. Most application code sees this information through {@link top.chiloven.mcsmp4j.McsmpRemoteException}; this
 * record is useful for diagnostics, low-level protocol tools, and extension clients that inspect raw responses.</p>
 *
 * @param code    the JSON-RPC error code
 * @param message the server-supplied error message
 * @param data    optional structured error data, or {@code null} when absent
 */
public record JsonRpcError(
        int code,
        String message,
        @Nullable JsonNode data
) {

}
