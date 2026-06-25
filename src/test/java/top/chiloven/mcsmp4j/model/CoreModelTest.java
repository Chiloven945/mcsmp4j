package top.chiloven.mcsmp4j.model;

import org.junit.jupiter.api.Test;
import top.chiloven.mcsmp4j.McsmpObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class CoreModelTest {

    @Test
    void playerRequiresAnIdOrName() {
        assertThatThrownBy(() -> new Player(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Either id or name");
    }

    @Test
    void serializesPlayerWithoutNullOptionals() throws Exception {
        var mapper = McsmpObjectMapper.create();
        var json = mapper.valueToTree(Player.byName("jeb_"));

        assertThat(json.has("id")).isFalse();
        assertThat(json.get("name").asString()).isEqualTo("jeb_");
    }

    @Test
    void serializesEnumProtocolValues() throws Exception {
        var mapper = McsmpObjectMapper.create();

        assertThat(mapper.writeValueAsString(Difficulty.HARD)).isEqualTo("\"hard\"");
        assertThat(mapper.readValue("\"creative\"", GameMode.class)).isEqualTo(GameMode.CREATIVE);
    }

    @Test
    void serializesSystemMessageForAllPlayersWithoutReceiverList() {
        var mapper = McsmpObjectMapper.create();
        var json = mapper.valueToTree(SystemMessage.chat(Message.literal("Hello")));

        assertThat(json.get("message").get("literal").asString()).isEqualTo("Hello");
        assertThat(json.get("overlay").asBoolean()).isFalse();
        assertThat(json.has("receivingPlayers")).isFalse();
    }

    @Test
    void serverStateCopiesPlayersDefensively() {
        var id = UUID.randomUUID();
        var players = List.of(Player.byId(id));

        var state = new ServerState(true, players, new MinecraftVersion("1.21.9", 771));

        assertThat(state.onlinePlayerCount()).isEqualTo(1);
        assertThat(state.players().getFirst().id()).isEqualTo(id);
    }

}
