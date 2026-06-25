package top.chiloven.mcsmp4j;

import java.net.http.WebSocket;

import static java.util.Objects.requireNonNull;

/**
 * Authentication strategy used during the WebSocket opening handshake.
 */
public sealed interface McsmpAuth permits
        McsmpAuth.Bearer,
        McsmpAuth.WebSocketSubprotocol,
        McsmpAuth.None {

    static McsmpAuth bearer(String secret) {
        return new Bearer(secret);
    }

    static McsmpAuth websocketSubprotocol(String secret) {
        return new WebSocketSubprotocol(secret);
    }

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

    void apply(WebSocket.Builder builder);

    enum None implements McsmpAuth {

        INSTANCE;

        @Override
        public void apply(WebSocket.Builder builder) {
            // Intentionally empty. Useful for tests and pre-authentication snapshots.
        }

    }

    record Bearer(
            String secret
    ) implements McsmpAuth {

        public Bearer {
            secret = requireSecret(secret);
        }

        @Override
        public void apply(WebSocket.Builder builder) {
            builder.header("Authorization", "Bearer " + secret);
        }

    }

    record WebSocketSubprotocol(
            String secret
    ) implements McsmpAuth {

        public WebSocketSubprotocol {
            secret = requireSecret(secret);
        }

        @Override
        public void apply(WebSocket.Builder builder) {
            builder.subprotocols("minecraft-v1", secret);
        }

    }

}
