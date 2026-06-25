package top.chiloven.mcsmp4j;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

/**
 * Raised when the server returns a JSON-RPC error object.
 */
public final class McsmpRemoteException extends McsmpException {

    private final int code;
    private final String remoteMessage;
    private final @Nullable JsonNode data;

    public McsmpRemoteException(
            int code,
            String remoteMessage,
            @Nullable JsonNode data
    ) {
        super("MCSMP remote error " + code + ": " + remoteMessage);
        this.code = code;
        this.remoteMessage = remoteMessage;
        this.data = data;
    }

    public int code() {
        return code;
    }

    public String remoteMessage() {
        return remoteMessage;
    }

    public @Nullable JsonNode data() {
        return data;
    }

}
