package top.chiloven.mcsmp4j;

/**
 * Raised when an MCSMP request does not receive a response before the configured request timeout.
 *
 * <p>The timeout duration is controlled by {@link McsmpClientConfig#requestTimeout()}. When this exception is
 * raised the underlying WebSocket may still be connected; callers may decide whether to continue using the client,
 * close it, or perform their own retry logic.</p>
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
