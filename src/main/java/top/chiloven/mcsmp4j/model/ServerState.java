package top.chiloven.mcsmp4j.model;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Current server lifecycle state and online player snapshot.
 *
 * <p>On protocol versions where the management endpoint is available before the Minecraft server has fully
 * started, {@link #started()} can be {@code false}. In that state, only a subset of methods and notifications may be
 * usable.</p>
 *
 * @param started whether the Minecraft dedicated server has completed startup
 * @param players the current online player snapshot
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
