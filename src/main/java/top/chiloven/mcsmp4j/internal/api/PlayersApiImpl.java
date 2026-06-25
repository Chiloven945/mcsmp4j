package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.PlayersApi;
import top.chiloven.mcsmp4j.model.KickPlayer;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class PlayersApiImpl implements PlayersApi {

    private static final String BASE = "minecraft:players";

    private final RawApi raw;

    public PlayersApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<Player>> list() {
        return ApiSupport.playerList(raw, BASE);
    }

    @Override
    public CompletableFuture<List<Player>> kick(Collection<KickPlayer> players) {
        return ApiSupport.playerList(raw, BASE + "/kick", ApiSupport.copy(players, "players"));
    }

}
