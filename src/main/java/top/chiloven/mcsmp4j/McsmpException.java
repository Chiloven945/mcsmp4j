package top.chiloven.mcsmp4j;

/**
 * Base class for all mcsmp4j-specific runtime exceptions.
 *
 * <p>The library uses unchecked exceptions because almost all public operations are asynchronous and report failures
 * by
 * completing {@link java.util.concurrent.CompletableFuture futures} exceptionally. Catch this base type when an
 * application wants to handle all client-library failures in one place, or catch one of the more specific subclasses
 * when presenting precise diagnostics to users.</p>
 *
 * <p>Subclasses distinguish the failure layer: authentication and connection problems during the WebSocket handshake,
 * protocol-shape problems while decoding JSON-RPC, remote JSON-RPC error objects returned by the server, request
 * timeouts, and feature checks that fail because a server does not expose the requested capability.</p>
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
