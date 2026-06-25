package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Player;
import top.chiloven.mcsmp4j.model.UserBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed API for {@code minecraft:bans}.
 */
public interface BansApi {

    CompletableFuture<List<UserBan>> list();

    CompletableFuture<List<UserBan>> clear();

    default CompletableFuture<List<UserBan>> set(UserBan... bans) {
        return set(List.of(bans));
    }

    CompletableFuture<List<UserBan>> set(Collection<UserBan> bans);

    default CompletableFuture<List<UserBan>> add(UserBan... bans) {
        return add(List.of(bans));
    }

    CompletableFuture<List<UserBan>> add(Collection<UserBan> bans);

    default CompletableFuture<List<UserBan>> remove(Player... players) {
        return remove(List.of(players));
    }

    CompletableFuture<List<UserBan>> remove(Collection<Player> players);

}
