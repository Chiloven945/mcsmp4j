package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:allowlist} method group.
 *
 * <p>The allowlist is the server-maintained list of player identities that may join when the server setting represented
 * by
 * {@link ServerSettingsApi#useAllowlist()} is enabled. This interface provides CRUD-style operations for that list and
 * returns the full list snapshot produced by the server after each mutation.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #list()} maps to {@code minecraft:allowlist}</li>
 *     <li>{@link #set(java.util.Collection)} maps to {@code minecraft:allowlist/set}</li>
 *     <li>{@link #add(java.util.Collection)} maps to {@code minecraft:allowlist/add}</li>
 *     <li>{@link #remove(java.util.Collection)} maps to {@code minecraft:allowlist/remove}</li>
 *     <li>{@link #clear()} maps to {@code minecraft:allowlist/clear}</li>
 * </ul>
 *
 * <p>Player objects may contain a UUID, a name, or both. Servers decide how names are resolved and whether partial
 * identifiers are accepted. When allowlist enforcement is enabled, removing an online player may cause the server to kick
 * that player immediately; listen for {@link top.chiloven.mcsmp4j.event.AllowlistRemovedEvent} and player leave events if
 * your application mirrors live state.</p>
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
