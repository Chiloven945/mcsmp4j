package top.chiloven.mcsmp4j;

/**
 * Base type for all mcsmp4j checked-at-runtime failures.
 */
public class McsmpException extends RuntimeException {

    public McsmpException(String message) {
        super(message);
    }

    public McsmpException(String message, Throwable cause) {
        super(message, cause);
    }

}
