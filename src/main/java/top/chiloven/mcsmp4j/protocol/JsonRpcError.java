package top.chiloven.mcsmp4j.protocol;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * Raw JSON-RPC 2.0 error payload returned by a server.
 *
 * <p>Most callers will see remote errors as {@link top.chiloven.mcsmp4j.McsmpRemoteException}. This record is
 * exposed for users who work directly with protocol-level objects or custom transports.</p>
 *
 * @param code    the numeric JSON-RPC error code
 * @param message the server-provided error message
 * @param data    optional implementation-defined error data
 */
public record JsonRpcError(
        int code,
        String message,
        @Nullable JsonNode data
) {

}
