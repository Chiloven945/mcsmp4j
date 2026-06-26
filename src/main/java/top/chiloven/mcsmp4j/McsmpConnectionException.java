package top.chiloven.mcsmp4j;

/**
 * Raised when the client cannot establish or maintain the WebSocket connection.
 *
 * <p>This exception is used for transport-level problems that are not specifically authentication failures:
 * unreachable hosts, TLS setup errors, failed handshakes, unexpected close frames, or I/O errors while sending or
 * receiving JSON-RPC messages.</p>
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
