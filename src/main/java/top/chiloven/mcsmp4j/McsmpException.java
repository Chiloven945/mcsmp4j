package top.chiloven.mcsmp4j;

/**
 * Base unchecked exception for all failures reported by mcsmp4j.
 *
 * <p>The client uses asynchronous APIs, so most failures are delivered by completing a
 * {@link java.util.concurrent.CompletableFuture} exceptionally with one of this class's subclasses. Callers that use
 * {@code join()} will see the exception wrapped in {@link java.util.concurrent.CompletionException}, while callers that
 * use {@code get()} will see it wrapped in {@link java.util.concurrent.ExecutionException}.</p>
 *
 * <p>Subclasses distinguish connection failures, authentication or origin rejection, malformed protocol data,
 * request timeouts, remote JSON-RPC errors, and unsupported optional protocol features.</p>
 */
public class McsmpException extends RuntimeException {

    /**
     * Creates an exception with a human-readable message.
     *
     * @param message a description of the failure
     */
    public McsmpException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a human-readable message and the original cause.
     *
     * @param message a description of the failure
     * @param cause   the underlying exception that caused this failure
     */
    public McsmpException(String message, Throwable cause) {
        super(message, cause);
    }

}
