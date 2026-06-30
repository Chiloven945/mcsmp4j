package top.chiloven.mcsmp4j;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * Exception representing a JSON-RPC error object returned by the server.
 *
 * <p>When a request reaches the server and the server responds with {@code error} instead of {@code result}, mcsmp4j
 * completes the request future exceptionally with this type. The exception preserves the numeric JSON-RPC code, the
 * remote message string, and optional structured error data. Applications building user interfaces should display the
 * remote message and may log the code/data for diagnostics.</p>
 *
 * <p>Common situations include unknown methods, invalid parameters, authorization decisions made by the server,
 * attempts to
 * modify unavailable state, or custom namespace errors. Because the request was processed by the remote endpoint,
 * blindly retrying may repeat the same logical error unless the input changes.</p>
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
