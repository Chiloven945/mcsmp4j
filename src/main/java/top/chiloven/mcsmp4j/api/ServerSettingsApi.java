package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Difficulty;
import top.chiloven.mcsmp4j.model.GameMode;

import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:serversettings} method group.
 *
 * <p>Server settings are mutable properties that affect joining, gameplay defaults, server visibility, status
 * behavior,
 * simulation radius, and networking behavior. Each setting is represented by a getter and a setter whose result is the
 * server-confirmed value after the operation. This design lets management tools update UI state directly from the
 * returned future rather than assuming the requested value was accepted unchanged.</p>
 *
 * <h2>Protocol mapping pattern</h2>
 *
 * <p>Getter methods map to paths such as {@code minecraft:serversettings/max_players}. Setter methods map to the
 * corresponding {@code /set} path such as {@code minecraft:serversettings/max_players/set}. Java names use standard
 * camel case while preserving the protocol setting name in Javadoc and implementation tests.</p>
 *
 * <h2>Validation</h2>
 *
 * <p>mcsmp4j performs minimal client-side validation for obviously invalid values such as negative player counts or
 * blank
 * messages. The server remains the final authority: it may clamp values, reject values outside its own bounds, or apply
 * changes only after internal synchronization. Always prefer the returned value over the originally requested
 * value.</p>
 *
 * <h2>Operational notes</h2>
 *
 * <p>Some settings take effect immediately for connected players; others primarily affect future joins or status
 * replies.
 * For example, allowlist-related settings interact with the allowlist API, distance settings affect gameplay load, and
 * status settings affect external server-list queries. Administrative UI should group these operations carefully and
 * avoid changing several unrelated settings without clear operator intent.</p>
 */
public interface ServerSettingsApi {

    /**
     * Gets whether automatic world saving is enabled.
     *
     * @return a future containing the current autosave flag
     */
    CompletableFuture<Boolean> autosave();

    /**
     * Enables or disables automatic world saving.
     *
     * @param enabled {@code true} to enable autosave, {@code false} to disable it
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setAutosave(boolean enabled);

    /**
     * Gets the current server difficulty.
     *
     * @return a future containing the current difficulty
     */
    CompletableFuture<Difficulty> difficulty();

    /**
     * Sets the server difficulty.
     *
     * @param difficulty the target difficulty
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Difficulty> setDifficulty(Difficulty difficulty);

    /**
     * Gets whether allowlist enforcement is enabled.
     *
     * <p>When enforcement is enabled, players can be kicked immediately after being removed from the allowlist.</p>
     *
     * @return a future containing the current enforcement flag
     */
    CompletableFuture<Boolean> enforceAllowlist();

    /**
     * Enables or disables allowlist enforcement.
     *
     * @param enforced {@code true} to enforce allowlist removals immediately
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setEnforceAllowlist(boolean enforced);

    /**
     * Gets whether the allowlist is enabled for joining players.
     *
     * @return a future containing whether the server uses the allowlist
     */
    CompletableFuture<Boolean> useAllowlist();

    /**
     * Enables or disables the allowlist join check.
     *
     * @param used {@code true} to allow only allowlisted players to join
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setUseAllowlist(boolean used);

    /**
     * Gets the maximum number of players allowed to connect.
     *
     * @return a future containing the current maximum player count
     */
    CompletableFuture<Integer> maxPlayers();

    /**
     * Sets the maximum number of players allowed to connect.
     *
     * @param maxPlayers the new maximum player count
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setMaxPlayers(int maxPlayers);

    /**
     * Gets the number of seconds before the server automatically pauses when no players are online.
     *
     * @return a future containing the current empty-server pause delay in seconds
     */
    CompletableFuture<Integer> pauseWhenEmptySeconds();

    /**
     * Sets the number of seconds before the server automatically pauses when no players are online.
     *
     * @param seconds the new empty-server pause delay in seconds
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setPauseWhenEmptySeconds(int seconds);

    /**
     * Gets the number of seconds before idle players are automatically kicked.
     *
     * @return a future containing the current idle timeout in seconds
     */
    CompletableFuture<Integer> playerIdleTimeout();

    /**
     * Sets the number of seconds before idle players are automatically kicked.
     *
     * @param seconds the new idle timeout in seconds
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setPlayerIdleTimeout(int seconds);

    /**
     * Gets whether flight is allowed for players in Survival mode.
     *
     * @return a future containing the current allow-flight flag
     */
    CompletableFuture<Boolean> allowFlight();

    /**
     * Sets whether flight is allowed for players in Survival mode.
     *
     * @param allowed {@code true} to allow Survival-mode flight
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setAllowFlight(boolean allowed);

    /**
     * Gets the server message of the day displayed to players.
     *
     * @return a future containing the current MOTD string
     */
    CompletableFuture<String> motd();

    /**
     * Sets the server message of the day displayed to players.
     *
     * @param message the new MOTD text
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<String> setMotd(String message);

    /**
     * Gets the spawn protection radius in blocks.
     *
     * @return a future containing the current spawn protection radius
     */
    CompletableFuture<Integer> spawnProtectionRadius();

    /**
     * Sets the spawn protection radius in blocks.
     *
     * @param radius the new spawn protection radius
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setSpawnProtectionRadius(int radius);

    /**
     * Gets whether joining players are forced into the server's default game mode.
     *
     * @return a future containing the current force-game-mode flag
     */
    CompletableFuture<Boolean> forceGameMode();

    /**
     * Sets whether joining players are forced into the server's default game mode.
     *
     * @param forced {@code true} to force the default game mode
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setForceGameMode(boolean forced);

    /**
     * Gets the server's default game mode.
     *
     * @return a future containing the current default game mode
     */
    CompletableFuture<GameMode> gameMode();

    /**
     * Sets the server's default game mode.
     *
     * @param mode the target default game mode
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<GameMode> setGameMode(GameMode mode);

    /**
     * Gets the server view distance in chunks.
     *
     * @return a future containing the current view distance
     */
    CompletableFuture<Integer> viewDistance();

    /**
     * Sets the server view distance in chunks.
     *
     * @param distance the target view distance in chunks
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setViewDistance(int distance);

    /**
     * Gets the server simulation distance in chunks.
     *
     * @return a future containing the current simulation distance
     */
    CompletableFuture<Integer> simulationDistance();

    /**
     * Sets the server simulation distance in chunks.
     *
     * @param distance the target simulation distance in chunks
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setSimulationDistance(int distance);

    /**
     * Gets whether the server accepts player transfers from other servers.
     *
     * @return a future containing the current transfer-acceptance flag
     */
    CompletableFuture<Boolean> acceptTransfers();

    /**
     * Sets whether the server accepts player transfers from other servers.
     *
     * @param accepted {@code true} to accept transfers
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setAcceptTransfers(boolean accepted);

    /**
     * Gets the interval between server status heartbeat notifications.
     *
     * @return a future containing the current heartbeat interval in seconds
     */
    CompletableFuture<Integer> statusHeartbeatInterval();

    /**
     * Sets the interval between server status heartbeat notifications.
     *
     * @param seconds the new heartbeat interval in seconds
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setStatusHeartbeatInterval(int seconds);

    /**
     * Gets the permission level required for operator commands.
     *
     * @return a future containing the current operator command permission level
     */
    CompletableFuture<Integer> operatorUserPermissionLevel();

    /**
     * Sets the permission level required for operator commands.
     *
     * @param level the new operator command permission level
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setOperatorUserPermissionLevel(int level);

    /**
     * Gets whether online player information is hidden from status queries.
     *
     * @return a future containing the current hide-online-players flag
     */
    CompletableFuture<Boolean> hideOnlinePlayers();

    /**
     * Sets whether online player information is hidden from status queries.
     *
     * @param hidden {@code true} to hide online player information
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setHideOnlinePlayers(boolean hidden);

    /**
     * Gets whether the server responds to connection status requests.
     *
     * @return a future containing the current status-replies flag
     */
    CompletableFuture<Boolean> statusReplies();

    /**
     * Sets whether the server responds to connection status requests.
     *
     * @param enabled {@code true} to respond to status requests
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Boolean> setStatusReplies(boolean enabled);

    /**
     * Gets the entity broadcast range as percentage points.
     *
     * @return a future containing the current broadcast range percentage
     */
    CompletableFuture<Integer> entityBroadcastRange();

    /**
     * Sets the entity broadcast range as percentage points.
     *
     * @param percentagePoints the target broadcast range percentage points
     *
     * @return a future containing the value accepted by the server
     */
    CompletableFuture<Integer> setEntityBroadcastRange(int percentagePoints);

}
