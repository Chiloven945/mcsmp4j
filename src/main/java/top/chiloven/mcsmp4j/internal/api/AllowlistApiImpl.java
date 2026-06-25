package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.AllowlistApi;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class AllowlistApiImpl implements AllowlistApi {

    private static final String BASE = "minecraft:allowlist";

    private final RawApi raw;

    public AllowlistApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<Player>> list() {
        return ApiSupport.playerList(raw, BASE);
    }

    @Override
    public CompletableFuture<List<Player>> clear() {
        return ApiSupport.playerList(raw, BASE + "/clear");
    }

    @Override
    public CompletableFuture<List<Player>> set(Collection<Player> players) {
        return ApiSupport.playerList(raw, BASE + "/set", ApiSupport.copy(players, "players"));
    }

    @Override
    public CompletableFuture<List<Player>> add(Collection<Player> players) {
        return ApiSupport.playerList(raw, BASE + "/add", ApiSupport.copy(players, "players"));
    }

    @Override
    public CompletableFuture<List<Player>> remove(Collection<Player> players) {
        return ApiSupport.playerList(raw, BASE + "/remove", ApiSupport.copy(players, "players"));
    }

}
