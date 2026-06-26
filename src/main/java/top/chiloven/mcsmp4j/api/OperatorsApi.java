package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Operator;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:operators} method group.
 *
 * <p>Operators are players with elevated command permissions. The protocol exposes the operator list as a full
 * list resource: operations that mutate it return the complete operator snapshot after the change.</p>
 */
public interface OperatorsApi {

    /**
     * Gets the current operator list.
     *
     * @return a future containing all operator entries known to the server
     */
    CompletableFuture<List<Operator>> list();

    /**
     * Removes all operators.
     *
     * @return a future containing the operator list after clearing, usually an empty list
     */
    CompletableFuture<List<Operator>> clear();

    /**
     * Replaces the operator list with the supplied entries.
     *
     * @param operators the entries that should become the complete operator list
     *
     * @return a future containing the operator list after replacement
     */
    default CompletableFuture<List<Operator>> set(Operator... operators) {
        return set(List.of(operators));
    }

    /**
     * Replaces the operator list with the supplied entries.
     *
     * @param operators the entries that should become the complete operator list
     *
     * @return a future containing the operator list after replacement
     */
    CompletableFuture<List<Operator>> set(Collection<Operator> operators);

    /**
     * Adds operator entries.
     *
     * @param operators the operator entries to add
     *
     * @return a future containing the operator list after the addition
     */
    default CompletableFuture<List<Operator>> add(Operator... operators) {
        return add(List.of(operators));
    }

    /**
     * Adds operator entries.
     *
     * @param operators the operator entries to add
     *
     * @return a future containing the operator list after the addition
     */
    CompletableFuture<List<Operator>> add(Collection<Operator> operators);

    /**
     * Removes operator entries for the supplied players.
     *
     * @param players the players to de-op
     *
     * @return a future containing the operator list after removal
     */
    default CompletableFuture<List<Operator>> remove(Player... players) {
        return remove(List.of(players));
    }

    /**
     * Removes operator entries for the supplied players.
     *
     * @param players the players to de-op
     *
     * @return a future containing the operator list after removal
     */
    CompletableFuture<List<Operator>> remove(Collection<Player> players);

}
