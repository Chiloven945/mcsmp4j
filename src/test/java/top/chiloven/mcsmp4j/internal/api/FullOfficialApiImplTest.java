package top.chiloven.mcsmp4j.internal.api;

import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

final class FullOfficialApiImplTest {

    @Test
    void bansApiUsesExpectedMethodsAndPayloads() {
        var ban = UserBan.byName("Notch");
        var raw = new RecordingRawApi(List.of(ban));
        var api = new BansApiImpl(raw);

        assertThat(api.list().join()).containsExactly(ban);
        api.set(ban).join();
        api.add(ban).join();
        api.remove(Player.byName("Notch")).join();
        api.clear().join();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:bans",
                        "minecraft:bans/set",
                        "minecraft:bans/add",
                        "minecraft:bans/remove",
                        "minecraft:bans/clear"
                );
        assertThat(raw.calls().get(1).params()).containsExactly(List.of(ban));
        assertThat(raw.calls().get(3).params()).containsExactly(List.of(Player.byName("Notch")));
    }

    @Test
    void ipBansApiUsesExpectedMethodsAndPayloads() {
        var ban = IpBan.permanent("192.0.2.10");
        var incoming = IncomingIpBan.byIp("192.0.2.10");
        var raw = new RecordingRawApi(List.of(ban));
        var api = new IpBansApiImpl(raw);

        api.list().join();
        api.set(ban).join();
        api.add(incoming).join();
        api.remove("192.0.2.10").join();
        api.clear().join();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:ip_bans",
                        "minecraft:ip_bans/set",
                        "minecraft:ip_bans/add",
                        "minecraft:ip_bans/remove",
                        "minecraft:ip_bans/clear"
                );
        assertThat(raw.calls().get(2).params()).containsExactly(List.of(incoming));
        assertThat(raw.calls().get(3).params()).containsExactly(List.of("192.0.2.10"));
    }

    @Test
    void operatorsApiUsesExpectedMethodsAndPayloads() {
        var operator = Operator.of(Player.byName("jeb_"), 4, true);
        var raw = new RecordingRawApi(List.of(operator));
        var api = new OperatorsApiImpl(raw);

        api.list().join();
        api.set(operator).join();
        api.add(operator).join();
        api.remove(Player.byName("jeb_")).join();
        api.clear().join();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:operators",
                        "minecraft:operators/set",
                        "minecraft:operators/add",
                        "minecraft:operators/remove",
                        "minecraft:operators/clear"
                );
        assertThat(raw.calls().get(1).params()).containsExactly(List.of(operator));
        assertThat(raw.calls().get(3).params()).containsExactly(List.of(Player.byName("jeb_")));
    }

    @Test
    void gamerulesApiUsesExpectedMethodsAndPayloads() {
        var typed = new TypedGameRule("doDaylightCycle", GameRuleType.BOOLEAN, GameRuleValue.of(false));
        var update = UntypedGameRule.bool("doDaylightCycle", false);
        var raw = new RecordingRawApi(typed);
        var api = new GamerulesApiImpl(raw);

        assertThat(api.update(update).join()).isEqualTo(typed);

        assertThat(raw.calls()).hasSize(1);
        assertThat(raw.calls().getFirst().method()).isEqualTo("minecraft:gamerules/update");
        assertThat(raw.calls().getFirst().params()).containsExactly(update);
    }

    @Test
    void serverSettingsApiUsesAllOfficialPaths() {
        var raw = new RecordingRawApi(Boolean.TRUE);
        var api = new ServerSettingsApiImpl(raw);

        api.autosave().join();
        api.setAutosave(false).join();
        api.enforceAllowlist().join();
        api.setEnforceAllowlist(true).join();
        api.useAllowlist().join();
        api.setUseAllowlist(true).join();
        api.allowFlight().join();
        api.setAllowFlight(false).join();
        api.forceGameMode().join();
        api.setForceGameMode(true).join();
        api.acceptTransfers().join();
        api.setAcceptTransfers(true).join();
        api.hideOnlinePlayers().join();
        api.setHideOnlinePlayers(true).join();
        api.statusReplies().join();
        api.setStatusReplies(true).join();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:serversettings/autosave",
                        "minecraft:serversettings/autosave/set",
                        "minecraft:serversettings/enforce_allowlist",
                        "minecraft:serversettings/enforce_allowlist/set",
                        "minecraft:serversettings/use_allowlist",
                        "minecraft:serversettings/use_allowlist/set",
                        "minecraft:serversettings/allow_flight",
                        "minecraft:serversettings/allow_flight/set",
                        "minecraft:serversettings/force_game_mode",
                        "minecraft:serversettings/force_game_mode/set",
                        "minecraft:serversettings/accept_transfers",
                        "minecraft:serversettings/accept_transfers/set",
                        "minecraft:serversettings/hide_online_players",
                        "minecraft:serversettings/hide_online_players/set",
                        "minecraft:serversettings/status_replies",
                        "minecraft:serversettings/status_replies/set"
                );
        assertThat(raw.calls().get(1).params()).containsExactly(false);
    }

    @Test
    void serverSettingsNumericAndEnumPathsUseExpectedMethods() {
        var raw = new RecordingRawApi(1);
        var api = new ServerSettingsApiImpl(raw);

        api.maxPlayers().join();
        api.setMaxPlayers(20).join();
        api.pauseWhenEmptySeconds().join();
        api.setPauseWhenEmptySeconds(60).join();
        api.playerIdleTimeout().join();
        api.setPlayerIdleTimeout(300).join();
        api.spawnProtectionRadius().join();
        api.setSpawnProtectionRadius(16).join();
        api.viewDistance().join();
        api.setViewDistance(10).join();
        api.simulationDistance().join();
        api.setSimulationDistance(10).join();
        api.statusHeartbeatInterval().join();
        api.setStatusHeartbeatInterval(5).join();
        api.operatorUserPermissionLevel().join();
        api.setOperatorUserPermissionLevel(4).join();
        api.entityBroadcastRange().join();
        api.setEntityBroadcastRange(100).join();

        assertThat(raw.calls()).extracting(RecordingRawApi.Call::method)
                .containsExactly(
                        "minecraft:serversettings/max_players",
                        "minecraft:serversettings/max_players/set",
                        "minecraft:serversettings/pause_when_empty_seconds",
                        "minecraft:serversettings/pause_when_empty_seconds/set",
                        "minecraft:serversettings/player_idle_timeout",
                        "minecraft:serversettings/player_idle_timeout/set",
                        "minecraft:serversettings/spawn_protection_radius",
                        "minecraft:serversettings/spawn_protection_radius/set",
                        "minecraft:serversettings/view_distance",
                        "minecraft:serversettings/view_distance/set",
                        "minecraft:serversettings/simulation_distance",
                        "minecraft:serversettings/simulation_distance/set",
                        "minecraft:serversettings/status_heartbeat_interval",
                        "minecraft:serversettings/status_heartbeat_interval/set",
                        "minecraft:serversettings/operator_user_permission_level",
                        "minecraft:serversettings/operator_user_permission_level/set",
                        "minecraft:serversettings/entity_broadcast_range",
                        "minecraft:serversettings/entity_broadcast_range/set"
                );
        assertThat(raw.calls().get(1).params()).containsExactly(20);
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
