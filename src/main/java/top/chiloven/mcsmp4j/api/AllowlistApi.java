package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:allowlist} method group.
 *
 * <p>The allowlist controls which players are permitted to join when the server setting
 * {@code use_allowlist} is enabled. Methods in this interface mirror the protocol endpoints and return the complete
 * allowlist snapshot after the operation completes.</p>
 *
 * <p>Player values may identify users by UUID, name, or both. The server decides how strictly names and UUIDs
 * are validated.</p>
 */
public interface AllowlistApi {

    /**
     * Gets the current allowlist.
     *
     * @return a future containing the server's current allowlisted players
     */
    CompletableFuture<List<Player>> list();

    /**
     * Clears all entries from the allowlist.
     *
     * @return a future containing the allowlist after it has been cleared, usually an empty list
     */
    CompletableFuture<List<Player>> clear();

    /**
     * Replaces the allowlist with the supplied players.
     *
     * @param players the players that should become the complete allowlist
     *
     * @return a future containing the allowlist after replacement
     */
    default CompletableFuture<List<Player>> set(Player... players) {
        return set(Arrays.asList(players));
    }

    /**
     * Replaces the allowlist with the supplied players.
     *
     * @param players the players that should become the complete allowlist
     *
     * @return a future containing the allowlist after replacement
     */
    CompletableFuture<List<Player>> set(Collection<Player> players);

    /**
     * Adds one or more players to the allowlist.
     *
     * @param players the players to add
     *
     * @return a future containing the allowlist after the addition
     */
    default CompletableFuture<List<Player>> add(Player... players) {
        return add(Arrays.asList(players));
    }

    /**
     * Adds one or more players to the allowlist.
     *
     * @param players the players to add
     *
     * @return a future containing the allowlist after the addition
     */
    CompletableFuture<List<Player>> add(Collection<Player> players);

    /**
     * Removes one or more players from the allowlist.
     *
     * @param players the players to remove
     *
     * @return a future containing the allowlist after the removal
     */
    default CompletableFuture<List<Player>> remove(Player... players) {
        return remove(Arrays.asList(players));
    }

    /**
     * Removes one or more players from the allowlist.
     *
     * @param players the players to remove
     *
     * @return a future containing the allowlist after the removal
     */
    CompletableFuture<List<Player>> remove(Collection<Player> players);

}
