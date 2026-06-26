package top.chiloven.mcsmp4j.internal;

import org.junit.jupiter.api.Test;
import top.chiloven.mcsmp4j.McsmpAuth;
import top.chiloven.mcsmp4j.McsmpAuthenticationException;
import top.chiloven.mcsmp4j.McsmpClient;
import top.chiloven.mcsmp4j.event.PlayerJoinedEvent;
import top.chiloven.mcsmp4j.model.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class JsonRpcTransportMockServerTest {

    @Test
    void performsWebSocketJsonRpcRoundTripAndSendsHandshakeHeaders() throws Exception {
        try (
                var server = MockMcsmpWebSocketServer.start(session -> {
                    session.accept();
                    var request = session.readText();
                    assertThat(request).contains("\"method\":\"minecraft:server/status\"");
                    assertThat(request).contains("\"id\":1");
                    session.sendText("""
                            {"jsonrpc":"2.0","id":1,"result":{"started":true,"players":[],"version":{"name":"1.21.9","protocol":771}}}
                            """);
                    session.readText(); // close frame from client
                })
        ) {
            try (
                    var client = McsmpClient.builder()
                            .endpoint(server.uri())
                            .auth(McsmpAuth.bearer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMN"))
                            .origin("mcsmp4j-test")
                            .requestTimeout(Duration.ofSeconds(5))
                            .build()
            ) {
                client.connect().join();

                var status = client.server().status().join();

                assertThat(status.started()).isTrue();
                assertThat(status.version().name()).isEqualTo("1.21.9");
            }

            assertThat(server.handshake().header("authorization"))
                    .isEqualTo("Bearer abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMN");
            assertThat(server.handshake().header("origin")).isEqualTo("mcsmp4j-test");
            server.awaitDone();
        }
    }

    @Test
    void acceptsFragmentedTextResponses() throws Exception {
        try (
                var server = MockMcsmpWebSocketServer.start(session -> {
                    session.accept();
                    var request = session.readText();
                    assertThat(request).contains("\"method\":\"minecraft:players\"");
                    session.sendTextFragments(
                            "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":[{\"name\":",
                            "\"Alex\"}]}"
                    );
                    session.readText();
                })
        ) {
            try (
                    var client = McsmpClient.builder()
                            .endpoint(server.uri())
                            .requestTimeout(Duration.ofSeconds(5))
                            .build()
            ) {
                client.connect().join();

                assertThat(client.players().list().join()).containsExactly(Player.byName("Alex"));
            }

            server.awaitDone();
        }
    }

    @Test
    void dispatchesServerNotificationsFromMockServer() throws Exception {
        try (
                var server = MockMcsmpWebSocketServer.start(session -> {
                    session.accept();
                    session.sendText("""
                            {"jsonrpc":"2.0","method":"minecraft:notification/players/joined","params":[{"name":"Steve"}]}
                            """);
                    session.readText();
                })
        ) {
            var joined = new CopyOnWriteArrayList<PlayerJoinedEvent>();
            try (
                    var client = McsmpClient.builder()
                            .endpoint(server.uri())
                            .requestTimeout(Duration.ofSeconds(5))
                            .build()
            ) {
                client.events().on(PlayerJoinedEvent.class, joined::add);
                client.connect().join();

                awaitSize(joined, 1);
                assertThat(joined.getFirst().player()).isEqualTo(Player.byName("Steve"));
            }

            server.awaitDone();
        }
    }

    private static void awaitSize(List<?> values, int expectedSize) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
        while (System.nanoTime() < deadline) {
            if (values.size() == expectedSize) {
                return;
            }
            Thread.sleep(10);
        }
        assertThat(values).hasSize(expectedSize);
    }

    @Test
    void mapsHttp401HandshakeToAuthenticationException() throws Exception {
        try (var server = MockMcsmpWebSocketServer.rejecting(401, "Unauthorized")) {
            var client = McsmpClient.builder()
                    .endpoint(server.uri())
                    .requestTimeout(Duration.ofSeconds(5))
                    .build();

            assertThatThrownBy(() -> client.connect().join())
                    .isInstanceOf(CompletionException.class)
                    .hasCauseInstanceOf(McsmpAuthenticationException.class);

            server.awaitDone();
        }
    }

}
