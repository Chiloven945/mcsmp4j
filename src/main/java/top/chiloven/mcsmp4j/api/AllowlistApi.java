package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly-typed client for {@code minecraft:allowlist/*} methods.
 */
public interface AllowlistApi {

    CompletableFuture<List<Player>> list();

    CompletableFuture<List<Player>> clear();

    default CompletableFuture<List<Player>> set(Player... players) {
        return set(Arrays.asList(players));
    }

    CompletableFuture<List<Player>> set(Collection<Player> players);

    default CompletableFuture<List<Player>> add(Player... players) {
        return add(Arrays.asList(players));
    }

    CompletableFuture<List<Player>> add(Collection<Player> players);

    default CompletableFuture<List<Player>> remove(Player... players) {
        return remove(Arrays.asList(players));
    }

    CompletableFuture<List<Player>> remove(Collection<Player> players);

}
