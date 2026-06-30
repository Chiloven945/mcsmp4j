package top.chiloven.mcsmp4j.model;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Snapshot returned by the server status endpoint and status heartbeat notifications.
 *
 * <p>{@code ServerState} describes the dedicated server's lifecycle state, online-player snapshot, and reported
 * Minecraft
 * version at one point in time. It is not a live object. Later players joining/leaving or a server restarting do not
 * mutate an existing instance; subscribe to status and player events or call
 * {@link top.chiloven.mcsmp4j.api.ServerApi#status()} again to refresh state.</p>
 *
 * <h2>Pre-start management</h2>
 *
 * <p>Protocol versions that expose the management endpoint before the dedicated server finishes startup may return
 * {@link #started()} as {@code false}. In that state, dashboards can still show that management is reachable while
 * waiting for world load, but many gameplay-related methods may be unavailable or may return remote errors.</p>
 *
 * @param started whether the dedicated server has completed startup
 * @param players the online-player snapshot reported with this state
 * @param version the Minecraft version reported by the server
 */
public record ServerState(
        boolean started,
        List<Player> players,
        MinecraftVersion version
) {

    /**
     * Defensively copies the player snapshot and validates the version object.
     */
    public ServerState {
        players = List.copyOf(requireNonNull(players, "players"));
        requireNonNull(version, "version");
    }

    /**
     * Returns the number of players in the {@link #players()} snapshot.
     *
     * @return the online player count represented by this state object
     */
    public int onlinePlayerCount() {
        return players.size();
    }

}
