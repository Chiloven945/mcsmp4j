package top.chiloven.mcsmp4j.internal;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import top.chiloven.mcsmp4j.McsmpObjectMapper;
import top.chiloven.mcsmp4j.McsmpProtocolException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class JsonRpcCodecTest {

    private final ObjectMapper mapper = McsmpObjectMapper.create();
    private final JsonRpcCodec codec = new JsonRpcCodec(mapper);

    @Test
    void encodesRequestWithoutParams() throws Exception {
        var payload = codec.encodeRequest(1, "minecraft:server/status", new Object[0]);

        var node = mapper.readTree(payload);
        assertThat(node.get("jsonrpc").asString()).isEqualTo("2.0");
        assertThat(node.get("method").asString()).isEqualTo("minecraft:server/status");
        assertThat(node.get("id").asLong()).isEqualTo(1L);
        assertThat(node.has("params")).isFalse();
    }

    @Test
    void encodesRequestWithPositionalParams() throws Exception {
        var payload = codec.encodeRequest(
                2,
                "minecraft:allowlist/add",
                new Object[]{List.of(new PlayerParam("jeb_"))}
        );

        var params = mapper.readTree(payload).get("params");
        assertThat(params).hasSize(1);
        assertThat(params.get(0).get(0).get("name").asString()).isEqualTo("jeb_");
    }

    @Test
    void decodesResultResponse() {
        var message = codec.decode("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"ok\":true}} ");

        assertThat(message).isInstanceOf(JsonRpcCodec.ParsedMessage.Response.class);
        var response = ((JsonRpcCodec.ParsedMessage.Response) message).response();
        assertThat(response.idKey()).isEqualTo("1");
        assert response.result() != null;
        assertThat(response.result().get("ok").asBoolean()).isTrue();
        assertThat(response.error()).isNull();
    }

    @Test
    void decodesErrorResponse() {
        var message = codec.decode("""
                {"jsonrpc":"2.0","id":7,"error":{"code":-32601,"message":"Method not found","data":{"method":"missing"}}}
                """);

        var response = ((JsonRpcCodec.ParsedMessage.Response) message).response();
        assertThat(response.idKey()).isEqualTo("7");
        assertThat(response.error()).isNotNull();
        assertThat(response.error().code()).isEqualTo(-32601);
        assertThat(response.error().message()).isEqualTo("Method not found");
        assertThat(response.error().data().get("method").asString()).isEqualTo("missing");
    }


    @Test
    void decodesEmbeddedResultErrorAsRemoteErrorCompatibilityShape() {
        var message = codec.decode("""
                {"jsonrpc":"2.0","id":9,"result":{"error":{"code":-32601,"message":"Method not found","data":{"method":"missing"}}}}
                """);

        var response = ((JsonRpcCodec.ParsedMessage.Response) message).response();
        assertThat(response.idKey()).isEqualTo("9");
        assertThat(response.error()).isNotNull();
        assertThat(response.error().code()).isEqualTo(-32601);
        assertThat(response.error().message()).isEqualTo("Method not found");
    }

    @Test
    void decodesNotification() {
        var message = codec.decode("""
                {"jsonrpc":"2.0","method":"minecraft:notification/players/joined","params":[{"name":"Alex"}]}
                """);

        assertThat(message).isInstanceOf(JsonRpcCodec.ParsedMessage.Notification.class);
        var notification = ((JsonRpcCodec.ParsedMessage.Notification) message).notification();
        assertThat(notification.method()).isEqualTo("minecraft:notification/players/joined");
        assertThat(notification.params().get(0).get("name").asString()).isEqualTo("Alex");
    }

    @Test
    void rejectsInvalidJsonRpcMessage() {
        assertThatThrownBy(() -> codec.decode("[]"))
                .isInstanceOf(McsmpProtocolException.class)
                .hasMessageContaining("object");
    }

    private record PlayerParam(String name) {

    }

}
