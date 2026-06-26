package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.KickPlayer;
import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:players} method group.
 *
 * <p>This API provides a snapshot of currently connected players and can request that players be kicked. Kicking
 * returns the list of players the server actually kicked, which may be smaller than the requested list if some
 * selectors did not match currently connected players.</p>
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
