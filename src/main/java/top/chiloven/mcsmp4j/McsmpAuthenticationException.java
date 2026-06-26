package top.chiloven.mcsmp4j;

/**
 * Raised when the server rejects the WebSocket opening handshake for authentication or origin reasons.
 *
 * <p>Dedicated servers normally reject invalid secrets or disallowed {@code Origin} headers with HTTP 401.
 * This exception lets callers distinguish those failures from network-level connection errors.</p>
 */
public final class McsmpAuthenticationException extends McsmpException {

    public McsmpAuthenticationException(String message) {
        super(message);
    }

    public McsmpAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
