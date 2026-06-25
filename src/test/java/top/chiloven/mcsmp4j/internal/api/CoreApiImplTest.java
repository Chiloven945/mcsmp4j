package top.chiloven.mcsmp4j.internal.api;

import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.model.KickPlayer;
import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.Player;
import top.chiloven.mcsmp4j.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

final class CoreApiImplTest {

    @Test
    void allowlistAddUsesExpectedMethodAndSingleListParam() {
        var raw = new RecordingRawApi(List.of(Player.byName("jeb_")));
        var api = new AllowlistApiImpl(raw);

        var result = api.add(Player.byName("jeb_")).join();

        assertThat(result).containsExactly(Player.byName("jeb_"));
        assertThat(raw.calls()).hasSize(1);
        assertThat(raw.calls().getFirst().method()).isEqualTo("minecraft:allowlist/add");
        assertThat(raw.calls().getFirst().params()).containsExactly(List.of(Player.byName("jeb_")));
    }

    @Test
    void playersKickUsesExpectedMethodAndPayload() {
        var raw = new RecordingRawApi(List.of(Player.byName("Steve")));
        var api = new PlayersApiImpl(raw);
        var kick = KickPlayer.withMessage(Player.byName("Steve"), Message.literal("bye"));

        api.kick(kick).join();

        assertThat(raw.calls().getFirst().method()).isEqualTo("minecraft:players/kick");
        assertThat(raw.calls().getFirst().params()).containsExactly(List.of(kick));
    }

    @Test
    void serverMethodsUseExpectedMethods() {
        var raw = new RecordingRawApi(Boolean.TRUE);
        var api = new ServerApiImpl(raw);

        assertThat(api.save(true).join()).isTrue();
        assertThat(api.stop().join()).isTrue();
        assertThat(api.systemMessage(SystemMessage.chat(Message.literal("hello"))).join()).isTrue();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:server/save",
                        "minecraft:server/stop",
                        "minecraft:server/system_message"
                );
        assertThat(raw.calls().getFirst().params()).containsExactly(true);
    }

    private static final class RecordingRawApi implements RawApi {

        private final Object result;
        private final List<Call> calls = new ArrayList<>();

        private RecordingRawApi(Object result) {
            this.result = result;
        }

        List<Call> calls() {
            return calls;
        }

        @Override
        public CompletableFuture<JsonNode> callJson(
                String method,
                Object... params
        ) {
            throw new UnsupportedOperationException("not used by typed API tests");
        }

        @Override
        public <T> CompletableFuture<T> call(
                String method,
                Class<T> resultType,
                Object... params
        ) {
            calls.add(new Call(method, List.of(params)));
            return CompletableFuture.completedFuture(resultType.cast(result));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> CompletableFuture<T> call(
                String method,
                TypeReference<T> resultType,
                Object... params
        ) {
            calls.add(new Call(method, List.of(params)));
            return CompletableFuture.completedFuture((T) result);
        }

        private record Call(
                String method,
                List<Object> params
        ) {

        }

    }

}
