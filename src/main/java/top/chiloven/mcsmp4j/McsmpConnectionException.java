package top.chiloven.mcsmp4j;

/**
 * Raised when the WebSocket transport cannot connect, send, receive, or close cleanly.
 */
public final class McsmpConnectionException extends McsmpException {

    public McsmpConnectionException(String message) {
        super(message);
    }

    public McsmpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
