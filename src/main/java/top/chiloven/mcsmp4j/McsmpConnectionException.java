package top.chiloven.mcsmp4j;

/**
 * Exception raised for transport-level WebSocket connection failures.
 *
 * <p>Connection failures include DNS errors, refused TCP connections, TLS handshake problems, premature close frames,
 * unexpected I/O errors, and other failures that prevent the client from maintaining a usable WebSocket. Authentication
 * rejections are represented by {@link McsmpAuthenticationException}; server-side JSON-RPC errors are represented by
 * {@link McsmpRemoteException}.</p>
 */
public final class McsmpConnectionException extends McsmpException {

    /**
     * Creates a connection exception with a message.
     *
     * @param message a description of the connection failure
     */
    public McsmpConnectionException(String message) {
        super(message);
    }

    /**
     * Creates a connection exception with a message and underlying cause.
     *
     * @param message a description of the connection failure
     * @param cause   the lower-level exception raised by the transport
     */
    public McsmpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
