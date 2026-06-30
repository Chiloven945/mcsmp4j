package top.chiloven.mcsmp4j;

/**
 * Exception raised when a JSON-RPC request does not receive a matching response before the configured timeout.
 *
 * <p>A timeout is a client-side observation, not proof that the server did not execute the requested operation. The
 * server
 * may still be busy, the response may have been delayed, or the connection may be unhealthy. For operations with
 * lasting side effects, such as saving or changing server settings, applications should consider reading the resulting
 * state after reconnecting or after a longer timeout rather than immediately repeating the operation.</p>
 */
public final class McsmpTimeoutException extends McsmpException {

    /**
     * Creates a timeout exception with a message and underlying cause.
     *
     * @param message a description of the timed-out request
     * @param cause   the timeout cause supplied by the asynchronous runtime
     */
    public McsmpTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
