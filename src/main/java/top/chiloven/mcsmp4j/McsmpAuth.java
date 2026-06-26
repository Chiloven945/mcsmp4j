package top.chiloven.mcsmp4j;

import java.net.http.WebSocket;

import static java.util.Objects.requireNonNull;

/**
 * Describes how a client authenticates during the MCSMP WebSocket opening handshake.
 *
 * <p>Minecraft dedicated servers protect the management endpoint with a server-specific secret. The
 * protocol allows clients to present that secret either as an HTTP bearer token or as part of the
 * {@code Sec-WebSocket-Protocol} header. This sealed interface captures those supported handshake strategies while
 * keeping the transport implementation independent from the calling code.</p>
 *
 * <p>Most Java applications should use {@link #bearer(String)} because it maps directly to the
 * {@code Authorization: Bearer <secret>} header. Browser-like clients, or applications that need to mimic the
 * JavaScript WebSocket constructor behavior, can use {@link #websocketSubprotocol(String)}. {@link #none()} is intended
 * for tests and for older experimental server snapshots that did not yet require authentication.</p>
 */
public sealed interface McsmpAuth permits
        McsmpAuth.Bearer,
        McsmpAuth.WebSocketSubprotocol,
        McsmpAuth.None {

    /**
     * Creates an authentication strategy that sends the server secret in the HTTP {@code Authorization} header as a
     * bearer token.
     *
     * @param secret the non-blank management-server secret configured by the Minecraft dedicated server
     *
     * @return an authentication strategy for the standard bearer-token handshake
     *
     * @throws NullPointerException     if {@code secret} is {@code null}
     * @throws IllegalArgumentException if {@code secret} is blank
     */
    static McsmpAuth bearer(String secret) {
        return new Bearer(secret);
    }

    /**
     * Creates an authentication strategy that sends {@code minecraft-v1} and the secret as WebSocket subprotocol
     * tokens.
     *
     * <p>This matches the protocol's browser-compatible authentication form where the secret follows the
     * {@code minecraft-v1} token in the {@code Sec-WebSocket-Protocol} header.</p>
     *
     * @param secret the non-blank management-server secret configured by the Minecraft dedicated server
     *
     * @return an authentication strategy for the WebSocket subprotocol handshake
     *
     * @throws NullPointerException     if {@code secret} is {@code null}
     * @throws IllegalArgumentException if {@code secret} is blank
     */
    static McsmpAuth websocketSubprotocol(String secret) {
        return new WebSocketSubprotocol(secret);
    }

    /**
     * Creates an authentication strategy that does not add any authentication data to the WebSocket handshake.
     *
     * <p>This is mainly useful for unit tests, local mock servers, and compatibility with pre-authentication
     * experimental snapshots. Production MCSMP endpoints are expected to reject unauthenticated clients.</p>
     *
     * @return the singleton no-authentication strategy
     */
    static McsmpAuth none() {
        return None.INSTANCE;
    }

    private static String requireSecret(String secret) {
        requireNonNull(secret, "secret");
        if (secret.isBlank()) {
            throw new IllegalArgumentException("secret must not be blank");
        }
        return secret;
    }

    /**
     * Applies this strategy to the JDK WebSocket builder before the opening handshake is started.
     *
     * @param builder the WebSocket builder that will create the management connection
     */
    void apply(WebSocket.Builder builder);

    /**
     * No-authentication strategy.
     *
     * <p>The enum is exposed as a public nested type only because sealed interfaces require their permitted
     * implementations to be named. Prefer the factory method {@link McsmpAuth#none()} in user code.</p>
     */
    enum None implements McsmpAuth {

        /**
         * The singleton no-authentication strategy.
         */
        INSTANCE;

        /**
         * Leaves the WebSocket builder unchanged.
         *
         * @param builder the builder to leave unchanged
         */
        @Override
        public void apply(WebSocket.Builder builder) {
            // Intentionally empty. Useful for tests and pre-authentication snapshots.
        }

    }

    /**
     * Bearer-token authentication strategy.
     *
     * @param secret the non-blank secret that will be sent as {@code Authorization: Bearer <secret>}
     */
    record Bearer(
            String secret
    ) implements McsmpAuth {

        /**
         * Validates the bearer secret.
         */
        public Bearer {
            secret = requireSecret(secret);
        }

        /**
         * Adds the {@code Authorization} header to the WebSocket handshake.
         *
         * @param builder the builder to configure
         */
        @Override
        public void apply(WebSocket.Builder builder) {
            builder.header("Authorization", "Bearer " + secret);
        }

    }

    /**
     * WebSocket subprotocol authentication strategy.
     *
     * @param secret the non-blank secret that will be sent after the {@code minecraft-v1} subprotocol token
     */
    record WebSocketSubprotocol(
            String secret
    ) implements McsmpAuth {

        /**
         * Validates the WebSocket subprotocol secret.
         */
        public WebSocketSubprotocol {
            secret = requireSecret(secret);
        }

        /**
         * Adds the {@code minecraft-v1} and secret subprotocol tokens to the WebSocket handshake.
         *
         * @param builder the builder to configure
         */
        @Override
        public void apply(WebSocket.Builder builder) {
            builder.subprotocols("minecraft-v1", secret);
        }

    }

}
