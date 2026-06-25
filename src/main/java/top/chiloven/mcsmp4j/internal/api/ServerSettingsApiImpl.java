package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.ServerSettingsApi;
import top.chiloven.mcsmp4j.model.Difficulty;
import top.chiloven.mcsmp4j.model.GameMode;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class ServerSettingsApiImpl implements ServerSettingsApi {

    private static final String BASE = "minecraft:serversettings";

    private final RawApi raw;

    public ServerSettingsApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<Boolean> autosave() {
        return bool("/autosave");
    }

    @Override
    public CompletableFuture<Boolean> setAutosave(boolean enabled) {
        return bool("/autosave/set", enabled);
    }

    @Override
    public CompletableFuture<Difficulty> difficulty() {
        return raw.call(BASE + "/difficulty", Difficulty.class);
    }

    @Override
    public CompletableFuture<Difficulty> setDifficulty(Difficulty difficulty) {
        return raw.call(BASE + "/difficulty/set", Difficulty.class, requireNonNull(difficulty, "difficulty"));
    }

    @Override
    public CompletableFuture<Boolean> enforceAllowlist() {
        return bool("/enforce_allowlist");
    }

    @Override
    public CompletableFuture<Boolean> setEnforceAllowlist(boolean enforced) {
        return bool("/enforce_allowlist/set", enforced);
    }

    @Override
    public CompletableFuture<Boolean> useAllowlist() {
        return bool("/use_allowlist");
    }

    @Override
    public CompletableFuture<Boolean> setUseAllowlist(boolean used) {
        return bool("/use_allowlist/set", used);
    }

    @Override
    public CompletableFuture<Integer> maxPlayers() {
        return integer("/max_players");
    }

    @Override
    public CompletableFuture<Integer> setMaxPlayers(int maxPlayers) {
        return integer("/max_players/set", ApiSupport.positive(maxPlayers, "maxPlayers"));
    }

    @Override
    public CompletableFuture<Integer> pauseWhenEmptySeconds() {
        return integer("/pause_when_empty_seconds");
    }

    @Override
    public CompletableFuture<Integer> setPauseWhenEmptySeconds(int seconds) {
        return integer("/pause_when_empty_seconds/set", ApiSupport.nonNegative(seconds, "seconds"));
    }

    @Override
    public CompletableFuture<Integer> playerIdleTimeout() {
        return integer("/player_idle_timeout");
    }

    @Override
    public CompletableFuture<Integer> setPlayerIdleTimeout(int seconds) {
        return integer("/player_idle_timeout/set", ApiSupport.nonNegative(seconds, "seconds"));
    }

    @Override
    public CompletableFuture<Boolean> allowFlight() {
        return bool("/allow_flight");
    }

    @Override
    public CompletableFuture<Boolean> setAllowFlight(boolean allowed) {
        return bool("/allow_flight/set", allowed);
    }

    @Override
    public CompletableFuture<String> motd() {
        return raw.call(BASE + "/motd", String.class);
    }

    @Override
    public CompletableFuture<String> setMotd(String message) {
        return raw.call(BASE + "/motd/set", String.class, requireNonNull(message, "message"));
    }

    @Override
    public CompletableFuture<Integer> spawnProtectionRadius() {
        return integer("/spawn_protection_radius");
    }

    @Override
    public CompletableFuture<Integer> setSpawnProtectionRadius(int radius) {
        return integer("/spawn_protection_radius/set", ApiSupport.nonNegative(radius, "radius"));
    }

    @Override
    public CompletableFuture<Boolean> forceGameMode() {
        return bool("/force_game_mode");
    }

    @Override
    public CompletableFuture<Boolean> setForceGameMode(boolean forced) {
        return bool("/force_game_mode/set", forced);
    }

    @Override
    public CompletableFuture<GameMode> gameMode() {
        return raw.call(BASE + "/game_mode", GameMode.class);
    }

    @Override
    public CompletableFuture<GameMode> setGameMode(GameMode mode) {
        return raw.call(BASE + "/game_mode/set", GameMode.class, requireNonNull(mode, "mode"));
    }

    @Override
    public CompletableFuture<Integer> viewDistance() {
        return integer("/view_distance");
    }

    @Override
    public CompletableFuture<Integer> setViewDistance(int distance) {
        return integer("/view_distance/set", ApiSupport.positive(distance, "distance"));
    }

    @Override
    public CompletableFuture<Integer> simulationDistance() {
        return integer("/simulation_distance");
    }

    @Override
    public CompletableFuture<Integer> setSimulationDistance(int distance) {
        return integer("/simulation_distance/set", ApiSupport.positive(distance, "distance"));
    }

    @Override
    public CompletableFuture<Boolean> acceptTransfers() {
        return bool("/accept_transfers");
    }

    @Override
    public CompletableFuture<Boolean> setAcceptTransfers(boolean accepted) {
        return bool("/accept_transfers/set", accepted);
    }

    @Override
    public CompletableFuture<Integer> statusHeartbeatInterval() {
        return integer("/status_heartbeat_interval");
    }

    @Override
    public CompletableFuture<Integer> setStatusHeartbeatInterval(int seconds) {
        return integer("/status_heartbeat_interval/set", ApiSupport.positive(seconds, "seconds"));
    }

    @Override
    public CompletableFuture<Integer> operatorUserPermissionLevel() {
        return integer("/operator_user_permission_level");
    }

    @Override
    public CompletableFuture<Integer> setOperatorUserPermissionLevel(int level) {
        return integer("/operator_user_permission_level/set", ApiSupport.nonNegative(level, "level"));
    }

    @Override
    public CompletableFuture<Boolean> hideOnlinePlayers() {
        return bool("/hide_online_players");
    }

    @Override
    public CompletableFuture<Boolean> setHideOnlinePlayers(boolean hidden) {
        return bool("/hide_online_players/set", hidden);
    }

    @Override
    public CompletableFuture<Boolean> statusReplies() {
        return bool("/status_replies");
    }

    @Override
    public CompletableFuture<Boolean> setStatusReplies(boolean enabled) {
        return bool("/status_replies/set", enabled);
    }

    @Override
    public CompletableFuture<Integer> entityBroadcastRange() {
        return integer("/entity_broadcast_range");
    }

    @Override
    public CompletableFuture<Integer> setEntityBroadcastRange(int percentagePoints) {
        return integer("/entity_broadcast_range/set", ApiSupport.nonNegative(percentagePoints, "percentagePoints"));
    }

    private CompletableFuture<Integer> integer(String path, Object... params) {
        return raw.call(BASE + path, Integer.class, params);
    }

    private CompletableFuture<Boolean> bool(String path, Object... params) {
        return raw.call(BASE + path, Boolean.class, params);
    }

}
