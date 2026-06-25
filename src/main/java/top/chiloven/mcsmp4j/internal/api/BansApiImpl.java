package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.BansApi;
import top.chiloven.mcsmp4j.model.Player;
import top.chiloven.mcsmp4j.model.UserBan;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class BansApiImpl implements BansApi {

    private static final String BASE = "minecraft:bans";

    private final RawApi raw;

    public BansApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<UserBan>> list() {
        return ApiSupport.userBanList(raw, BASE);
    }

    @Override
    public CompletableFuture<List<UserBan>> clear() {
        return ApiSupport.userBanList(raw, BASE + "/clear");
    }

    @Override
    public CompletableFuture<List<UserBan>> set(Collection<UserBan> bans) {
        return ApiSupport.userBanList(raw, BASE + "/set", ApiSupport.copy(bans, "bans"));
    }

    @Override
    public CompletableFuture<List<UserBan>> add(Collection<UserBan> bans) {
        return ApiSupport.userBanList(raw, BASE + "/add", ApiSupport.copy(bans, "bans"));
    }

    @Override
    public CompletableFuture<List<UserBan>> remove(Collection<Player> players) {
        return ApiSupport.userBanList(raw, BASE + "/remove", ApiSupport.copy(players, "players"));
    }

}
