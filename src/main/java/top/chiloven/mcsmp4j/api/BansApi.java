package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Player;
import top.chiloven.mcsmp4j.model.UserBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:bans} user-ban method group.
 *
 * <p>User bans prevent specific player identities from joining the server. This API manages bans represented by
 * {@link top.chiloven.mcsmp4j.model.UserBan}; removals use {@link top.chiloven.mcsmp4j.model.Player} selectors because
 * the server only needs to identify the banned player being removed.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #list()} maps to {@code minecraft:bans}</li>
 *     <li>{@link #set(java.util.Collection)} maps to {@code minecraft:bans/set}</li>
 *     <li>{@link #add(java.util.Collection)} maps to {@code minecraft:bans/add}</li>
 *     <li>{@link #remove(java.util.Collection)} maps to {@code minecraft:bans/remove}</li>
 *     <li>{@link #clear()} maps to {@code minecraft:bans/clear}</li>
 * </ul>
 *
 * <p>Ban records may contain a reason, source, and optional expiration instant. The server may normalize or enrich returned
 * ban entries, so treat method results as authoritative state snapshots.</p>
 */
public interface BansApi {

    /**
     * Gets the current user ban list.
     *
     * @return a future containing all user ban entries known to the server
     */
    CompletableFuture<List<UserBan>> list();

    /**
     * Clears the entire user ban list.
     *
     * @return a future containing the ban list after clearing, usually an empty list
     */
    CompletableFuture<List<UserBan>> clear();

    /**
     * Replaces the user ban list with the supplied entries.
     *
     * @param bans the entries that should become the complete ban list
     *
     * @return a future containing the ban list after replacement
     */
    default CompletableFuture<List<UserBan>> set(UserBan... bans) {
        return set(List.of(bans));
    }

    /**
     * Replaces the user ban list with the supplied entries.
     *
     * @param bans the entries that should become the complete ban list
     *
     * @return a future containing the ban list after replacement
     */
    CompletableFuture<List<UserBan>> set(Collection<UserBan> bans);

    /**
     * Adds user ban entries.
     *
     * @param bans the ban entries to add
     *
     * @return a future containing the ban list after the addition
     */
    default CompletableFuture<List<UserBan>> add(UserBan... bans) {
        return add(List.of(bans));
    }

    /**
     * Adds user ban entries.
     *
     * @param bans the ban entries to add
     *
     * @return a future containing the ban list after the addition
     */
    CompletableFuture<List<UserBan>> add(Collection<UserBan> bans);

    /**
     * Removes bans for the supplied players.
     *
     * @param players the players whose ban entries should be removed
     *
     * @return a future containing the ban list after the removal
     */
    default CompletableFuture<List<UserBan>> remove(Player... players) {
        return remove(List.of(players));
    }

    /**
     * Removes bans for the supplied players.
     *
     * @param players the players whose ban entries should be removed
     *
     * @return a future containing the ban list after the removal
     */
    CompletableFuture<List<UserBan>> remove(Collection<Player> players);

}
