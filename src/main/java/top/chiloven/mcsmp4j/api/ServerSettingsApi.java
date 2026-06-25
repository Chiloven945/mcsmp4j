package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Difficulty;
import top.chiloven.mcsmp4j.model.GameMode;

import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed API for {@code minecraft:serversettings}.
 */
public interface ServerSettingsApi {

    CompletableFuture<Boolean> autosave();

    CompletableFuture<Boolean> setAutosave(boolean enabled);

    CompletableFuture<Difficulty> difficulty();

    CompletableFuture<Difficulty> setDifficulty(Difficulty difficulty);

    CompletableFuture<Boolean> enforceAllowlist();

    CompletableFuture<Boolean> setEnforceAllowlist(boolean enforced);

    CompletableFuture<Boolean> useAllowlist();

    CompletableFuture<Boolean> setUseAllowlist(boolean used);

    CompletableFuture<Integer> maxPlayers();

    CompletableFuture<Integer> setMaxPlayers(int maxPlayers);

    CompletableFuture<Integer> pauseWhenEmptySeconds();

    CompletableFuture<Integer> setPauseWhenEmptySeconds(int seconds);

    CompletableFuture<Integer> playerIdleTimeout();

    CompletableFuture<Integer> setPlayerIdleTimeout(int seconds);

    CompletableFuture<Boolean> allowFlight();

    CompletableFuture<Boolean> setAllowFlight(boolean allowed);

    CompletableFuture<String> motd();

    CompletableFuture<String> setMotd(String message);

    CompletableFuture<Integer> spawnProtectionRadius();

    CompletableFuture<Integer> setSpawnProtectionRadius(int radius);

    CompletableFuture<Boolean> forceGameMode();

    CompletableFuture<Boolean> setForceGameMode(boolean forced);

    CompletableFuture<GameMode> gameMode();

    CompletableFuture<GameMode> setGameMode(GameMode mode);

    CompletableFuture<Integer> viewDistance();

    CompletableFuture<Integer> setViewDistance(int distance);

    CompletableFuture<Integer> simulationDistance();

    CompletableFuture<Integer> setSimulationDistance(int distance);

    CompletableFuture<Boolean> acceptTransfers();

    CompletableFuture<Boolean> setAcceptTransfers(boolean accepted);

    CompletableFuture<Integer> statusHeartbeatInterval();

    CompletableFuture<Integer> setStatusHeartbeatInterval(int seconds);

    CompletableFuture<Integer> operatorUserPermissionLevel();

    CompletableFuture<Integer> setOperatorUserPermissionLevel(int level);

    CompletableFuture<Boolean> hideOnlinePlayers();

    CompletableFuture<Boolean> setHideOnlinePlayers(boolean hidden);

    CompletableFuture<Boolean> statusReplies();

    CompletableFuture<Boolean> setStatusReplies(boolean enabled);

    CompletableFuture<Integer> entityBroadcastRange();

    CompletableFuture<Integer> setEntityBroadcastRange(int percentagePoints);

}
