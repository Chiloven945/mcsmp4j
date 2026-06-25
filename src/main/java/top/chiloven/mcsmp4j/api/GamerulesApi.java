package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.TypedGameRule;
import top.chiloven.mcsmp4j.model.UntypedGameRule;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed API for {@code minecraft:gamerules}.
 */
public interface GamerulesApi {

    CompletableFuture<List<TypedGameRule>> list();

    CompletableFuture<TypedGameRule> update(UntypedGameRule gamerule);

}
