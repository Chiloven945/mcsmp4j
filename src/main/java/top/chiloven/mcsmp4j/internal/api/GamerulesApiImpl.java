package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.GamerulesApi;
import top.chiloven.mcsmp4j.model.TypedGameRule;
import top.chiloven.mcsmp4j.model.UntypedGameRule;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class GamerulesApiImpl implements GamerulesApi {

    private static final String BASE = "minecraft:gamerules";

    private final RawApi raw;

    public GamerulesApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<TypedGameRule>> list() {
        return ApiSupport.typedGameRuleList(raw, BASE);
    }

    @Override
    public CompletableFuture<TypedGameRule> update(UntypedGameRule gamerule) {
        return raw.call(BASE + "/update", TypedGameRule.class, requireNonNull(gamerule, "gamerule"));
    }

}
