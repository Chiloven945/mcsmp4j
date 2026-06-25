package top.chiloven.mcsmp4j;

/**
 * Raised when a JSON-RPC request does not complete within the configured timeout.
 */
public final class McsmpTimeoutException extends McsmpException {

    public McsmpTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
