package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.TypedGameRule;
import top.chiloven.mcsmp4j.model.UntypedGameRule;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:gamerules} method group.
 *
 * <p>Game rules are dynamic world/server configuration values such as {@code doDaylightCycle} or
 * {@code randomTickSpeed}. Listing returns {@link top.chiloven.mcsmp4j.model.TypedGameRule} values, which include both
 * the server-declared type and the current value. Updating sends an {@link top.chiloven.mcsmp4j.model.UntypedGameRule}
 * because the server infers the expected type from the key.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #list()} maps to {@code minecraft:gamerules}</li>
 *     <li>{@link #update(top.chiloven.mcsmp4j.model.UntypedGameRule)} maps to {@code minecraft:gamerules/update}</li>
 * </ul>
 *
 * <p>Protocol version {@code 2.0.0} changed game-rule values from string values to typed JSON booleans and integers.
 * {@link top.chiloven.mcsmp4j.model.GameRuleValue} preserves both modern and legacy shapes so applications can interop
 * with a wider range of server builds.</p>
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
