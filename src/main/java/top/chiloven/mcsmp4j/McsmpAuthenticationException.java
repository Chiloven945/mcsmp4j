package top.chiloven.mcsmp4j;

/**
 * Raised when the server rejects the WebSocket opening handshake for authentication or origin reasons.
 *
 * <p>Dedicated servers normally reject invalid secrets or disallowed {@code Origin} headers with HTTP 401.
 * This exception lets callers distinguish those failures from network-level connection errors such as DNS, TCP, or TLS
 * failures.</p>
 */
public final class McsmpAuthenticationException extends McsmpException {

    /**
     * Creates an authentication exception with a message.
     *
     * @param message a description of the rejected handshake
     */
    public McsmpAuthenticationException(String message) {
        super(message);
    }

    /**
     * Creates an authentication exception with a message and underlying cause.
     *
     * @param message a description of the rejected handshake
     * @param cause   the lower-level exception raised by the WebSocket or HTTP client
     */
    public McsmpAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
