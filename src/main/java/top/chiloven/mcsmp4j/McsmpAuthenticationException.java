package top.chiloven.mcsmp4j;

/**
 * Exception raised when the server rejects the WebSocket opening handshake for authentication or authorization
 * reasons.
 *
 * <p>This usually means the management secret is missing, malformed, expired, or does not match
 * {@code management-server-secret}. It may also indicate that the server rejected the supplied {@code Origin} header
 * because it is not listed in {@code management-server-allowed-origins}. The exception is normally reported by
 * {@link McsmpClient#connect()}.</p>
 *
 * <p>Applications should treat this as a configuration or credential problem, not as a transient network failure.
 * Retrying
 * without changing the secret or origin usually produces the same result.</p>
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
