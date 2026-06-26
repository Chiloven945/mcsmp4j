package top.chiloven.mcsmp4j;

/**
 * Raised when the peer sends data that cannot be interpreted as valid MCSMP JSON-RPC traffic.
 *
 * <p>Examples include invalid JSON, a response with an unknown request id, a response that is missing both
 * {@code result} and {@code error}, or notification parameters that cannot be decoded into the expected event model. A
 * protocol exception usually indicates a server bug, a version mismatch, a custom namespace returning an unexpected
 * shape, or a malformed mock server in tests.</p>
 */
public final class McsmpProtocolException extends McsmpException {

    /**
     * Creates a protocol exception with a message.
     *
     * @param message a description of the malformed or unexpected protocol data
     */
    public McsmpProtocolException(String message) {
        super(message);
    }

    /**
     * Creates a protocol exception with a message and underlying cause.
     *
     * @param message a description of the malformed or unexpected protocol data
     * @param cause   the exception raised while parsing or mapping protocol data
     */
    public McsmpProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

}
