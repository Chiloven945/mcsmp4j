package top.chiloven.mcsmp4j.model;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Current server lifecycle state and online player snapshot.
 */
public record ServerState(
        boolean started,
        List<Player> players,
        MinecraftVersion version
) {

    public ServerState {
        players = List.copyOf(requireNonNull(players, "players"));
        requireNonNull(version, "version");
    }

    public int onlinePlayerCount() {
        return players.size();
    }

}
