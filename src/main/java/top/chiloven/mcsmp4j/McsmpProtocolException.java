package top.chiloven.mcsmp4j;

/**
 * Raised when a peer sends malformed or unsupported JSON-RPC data.
 */
public final class McsmpProtocolException extends McsmpException {

    public McsmpProtocolException(String message) {
        super(message);
    }

    public McsmpProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

}
