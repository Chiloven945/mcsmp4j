package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.TypedGameRule;
import top.chiloven.mcsmp4j.model.UntypedGameRule;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:gamerules} method group.
 *
 * <p>Game rules are identified by string keys and have server-declared types. Listing returns typed values, while
 * updates use {@link UntypedGameRule} because the server infers the expected type from the key.</p>
 */
public interface GamerulesApi {

    /**
     * Gets all available game rules and their current values.
     *
     * @return a future containing the current typed game-rule list
     */
    CompletableFuture<List<TypedGameRule>> list();

    /**
     * Updates one game-rule value.
     *
     * @param gamerule the game-rule key and new value
     *
     * @return a future containing the updated game rule as returned by the server
     */
    CompletableFuture<TypedGameRule> update(UntypedGameRule gamerule);

}
