package top.chiloven.mcsmp4j.internal.event;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import top.chiloven.mcsmp4j.McsmpObjectMapper;
import top.chiloven.mcsmp4j.event.PlayerJoinedEvent;
import top.chiloven.mcsmp4j.event.ServerStartedEvent;
import top.chiloven.mcsmp4j.event.WorldUpgradeProgressEvent;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;

import static org.assertj.core.api.Assertions.assertThat;

final class McsmpEventDecoderTest {

    private final ObjectMapper mapper = McsmpObjectMapper.create();

    @Test
    void decodesPlayerJoinedFromPositionalParams() throws Exception {
        var decoder = new McsmpEventDecoder(mapper, false);
        var event = decoder.decode(new JsonRpcNotification(
                PlayerJoinedEvent.METHOD,
                mapper.readTree("[{\"name\":\"Alex\"}]")
        ));

        assertThat(event).containsInstanceOf(PlayerJoinedEvent.class);
        var joined = (PlayerJoinedEvent) event.orElseThrow();
        assertThat(joined.player().name()).isEqualTo("Alex");
    }

    @Test
    void decodesServerStartedWithoutParams() throws Exception {
        var decoder = new McsmpEventDecoder(mapper, false);
        var event = decoder.decode(new JsonRpcNotification(
                ServerStartedEvent.METHOD,
                mapper.readTree("null")
        ));

        assertThat(event).contains(new ServerStartedEvent());
    }

    @Test
    void decodesWorldUpgradeProgressFromNamedParams() throws Exception {
        var decoder = new McsmpEventDecoder(mapper, false);
        var event = decoder.decode(new JsonRpcNotification(
                WorldUpgradeProgressEvent.METHOD,
                mapper.readTree("{\"progress\":0.75}")
        ));

        assertThat(event).contains(new WorldUpgradeProgressEvent(0.75d));
    }

    @Test
    void legacyNotificationPrefixIsOptional() throws Exception {
        var params = mapper.readTree("[{\"name\":\"Steve\"}]");

        assertThat(new McsmpEventDecoder(mapper, false)
                .decode(new JsonRpcNotification("notification:players/joined", params)))
                .isEmpty();

        assertThat(new McsmpEventDecoder(mapper, true)
                .decode(new JsonRpcNotification("notification:players/joined", params)))
                .containsInstanceOf(PlayerJoinedEvent.class);
    }

}
