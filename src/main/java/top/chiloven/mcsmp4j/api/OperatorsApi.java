package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Operator;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed API for {@code minecraft:operators}.
 */
public interface OperatorsApi {

    CompletableFuture<List<Operator>> list();

    CompletableFuture<List<Operator>> clear();

    default CompletableFuture<List<Operator>> set(Operator... operators) {
        return set(List.of(operators));
    }

    CompletableFuture<List<Operator>> set(Collection<Operator> operators);

    default CompletableFuture<List<Operator>> add(Operator... operators) {
        return add(List.of(operators));
    }

    CompletableFuture<List<Operator>> add(Collection<Operator> operators);

    default CompletableFuture<List<Operator>> remove(Player... players) {
        return remove(List.of(players));
    }

    CompletableFuture<List<Operator>> remove(Collection<Player> players);

}
