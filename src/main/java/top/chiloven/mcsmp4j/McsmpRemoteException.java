package top.chiloven.mcsmp4j;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * Raised when the server returns a JSON-RPC error response.
 *
 * <p>Unlike {@link McsmpProtocolException}, this exception means the response was well-formed but represented a
 * remote failure, such as an unknown method, invalid parameters, or a server-side operation error. The JSON-RPC error
 * code, message, and optional data object are retained so callers can implement precise handling.</p>
 */
public final class McsmpRemoteException extends McsmpException {

    /**
     * JSON-RPC error code returned by the server.
     */
    private final int code;
    /**
     * JSON-RPC error message returned by the server.
     */
    private final String remoteMessage;
    /**
     * Optional JSON-RPC error data returned by the server.
     */
    private final @Nullable JsonNode data;

    /**
     * Creates an exception from a JSON-RPC error object.
     *
     * @param code          the JSON-RPC error code returned by the server
     * @param remoteMessage the error message returned by the server
     * @param data          optional implementation-defined error data returned by the server
     */
    public McsmpRemoteException(
            int code,
            String remoteMessage,
            @Nullable JsonNode data
    ) {
        super("Remote JSON-RPC error " + code + ": " + remoteMessage);
        this.code = code;
        this.remoteMessage = remoteMessage;
        this.data = data;
    }

    /**
     * Returns the JSON-RPC error code.
     *
     * @return the numeric error code returned by the server
     */
    public int code() {
        return code;
    }

    /**
     * Returns the JSON-RPC error message.
     *
     * @return the server-provided error message
     */
    public String remoteMessage() {
        return remoteMessage;
    }

    /**
     * Returns optional implementation-defined JSON-RPC error data.
     *
     * @return the raw error {@code data} node, or {@code null} if the server omitted it
     */
    public @Nullable JsonNode data() {
        return data;
    }

}
