package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.KickPlayer;
import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:players} method group.
 *
 * <p>This API reads the current online player snapshot and requests that the server kick players. It operates on
 * {@link top.chiloven.mcsmp4j.model.Player} selectors rather than maintaining a persistent session object for each
 * player. Returned lists are snapshots, not live collections.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #list()} maps to {@code minecraft:players}</li>
 *     <li>{@link #kick(java.util.Collection)} maps to {@code minecraft:players/kick}</li>
 * </ul>
 *
 * <p>Kick requests use {@link top.chiloven.mcsmp4j.model.KickPlayer}, allowing an optional message to be sent to the
 * player. A successful result contains the players the server reports as kicked; it may be empty if the selectors did not
 * match any currently connected players.</p>
 */
public interface PlayersApi {

    /**
     * Gets all players currently connected to the server.
     *
     * @return a future containing the current online player snapshot
     */
    CompletableFuture<List<Player>> list();

    /**
     * Kicks a single player without a custom message.
     *
     * @param player the player to kick
     *
     * @return a future containing the players that were kicked
     */
    default CompletableFuture<List<Player>> kick(Player player) {
        return kick(KickPlayer.of(player));
    }

    /**
     * Kicks one or more players using full kick request objects.
     *
     * @param players the kick requests to send
     *
     * @return a future containing the players that were kicked
     */
    default CompletableFuture<List<Player>> kick(KickPlayer... players) {
        return kick(Arrays.asList(players));
    }

    /**
     * Kicks one or more players using full kick request objects.
     *
     * @param players the kick requests to send
     *
     * @return a future containing the players that were kicked
     */
    CompletableFuture<List<Player>> kick(Collection<KickPlayer> players);

    /**
     * Kicks a single player with a custom display message.
     *
     * @param player  the player to kick
     * @param message the message shown to the player, subject to server behavior
     *
     * @return a future containing the players that were kicked
     */
    default CompletableFuture<List<Player>> kick(Player player, Message message) {
        return kick(KickPlayer.withMessage(player, message));
    }

}
