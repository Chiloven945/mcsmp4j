package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.KickPlayer;
import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly-typed client for {@code minecraft:players/*} methods.
 */
public interface PlayersApi {

    CompletableFuture<List<Player>> list();

    default CompletableFuture<List<Player>> kick(Player player) {
        return kick(KickPlayer.of(player));
    }

    default CompletableFuture<List<Player>> kick(KickPlayer... players) {
        return kick(Arrays.asList(players));
    }

    CompletableFuture<List<Player>> kick(Collection<KickPlayer> players);

    default CompletableFuture<List<Player>> kick(Player player, Message message) {
        return kick(KickPlayer.withMessage(player, message));
    }

}
