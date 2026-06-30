package top.chiloven.mcsmp4j;

import java.net.http.WebSocket;

import static java.util.Objects.requireNonNull;

/**
 * Authentication strategy applied during the WebSocket opening handshake.
 *
 * <p>MCSMP authenticates clients with the server-specific management secret configured by the server operator. This
 * sealed interface models the supported ways to send that secret. The strategy is part of {@link McsmpClientConfig}; it
 * is used only during the opening handshake and is not sent again with each JSON-RPC request.</p>
 *
 * <h2>Bearer authentication</h2>
 *
 * <p>{@link #bearer(String)} sends the secret as an HTTP {@code Authorization: Bearer ...} header. This is the most
 * common choice for normal Java applications, command-line tools, daemons, and desktop applications.</p>
 *
 * <h2>WebSocket subprotocol authentication</h2>
 *
 * <p>{@link #websocketSubprotocol(String)} sends the secret using the {@code Sec-WebSocket-Protocol} header together
 * with the {@code minecraft-v1} subprotocol marker. This exists to match browser-style WebSocket construction rules and
 * may be useful for compatibility testing.</p>
 *
 * <h2>No authentication</h2>
 *
 * <p>{@link #none()} is intended only for mock servers, tests, or very old experimental endpoints. Production
 * Minecraft management servers should be configured with a secret and should reject unauthenticated clients.</p>
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
