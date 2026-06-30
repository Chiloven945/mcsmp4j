package top.chiloven.mcsmp4j;

/**
 * Exception raised when received data violates the JSON-RPC or MCSMP shape expected by the client.
 *
 * <p>This exception indicates that the WebSocket connection delivered a message but the message could not be
 * interpreted
 * as a valid response, error, or notification. Causes include malformed JSON, missing required JSON-RPC fields,
 * response ids that do not match pending requests, or payload structures that cannot be deserialized into the requested
 * model type.</p>
 *
 * <p>Protocol exceptions are useful bug reports for server implementations, proxy layers, or library compatibility with
 * a
 * new protocol revision. They are different from {@link McsmpRemoteException}, which means the server deliberately sent
 * a valid JSON-RPC error object.</p>
 */
public final class McsmpProtocolException extends McsmpException {

    /**
     * Creates a protocol exception with a message.
     *
     * @param message a description of the malformed or unexpected protocol data
     */
    public McsmpProtocolException(String message) {
        super(message);
    }

    /**
     * Creates a protocol exception with a message and underlying cause.
     *
     * @param message a description of the malformed or unexpected protocol data
     * @param cause   the exception raised while parsing or mapping protocol data
     */
    public McsmpProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

}
