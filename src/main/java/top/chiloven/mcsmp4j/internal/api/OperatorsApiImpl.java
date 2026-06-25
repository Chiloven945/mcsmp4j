package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.OperatorsApi;
import top.chiloven.mcsmp4j.model.Operator;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class OperatorsApiImpl implements OperatorsApi {

    private static final String BASE = "minecraft:operators";

    private final RawApi raw;

    public OperatorsApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<List<Operator>> list() {
        return ApiSupport.operatorList(raw, BASE);
    }

    @Override
    public CompletableFuture<List<Operator>> clear() {
        return ApiSupport.operatorList(raw, BASE + "/clear");
    }

    @Override
    public CompletableFuture<List<Operator>> set(Collection<Operator> operators) {
        return ApiSupport.operatorList(raw, BASE + "/set", ApiSupport.copy(operators, "operators"));
    }

    @Override
    public CompletableFuture<List<Operator>> add(Collection<Operator> operators) {
        return ApiSupport.operatorList(raw, BASE + "/add", ApiSupport.copy(operators, "operators"));
    }

    @Override
    public CompletableFuture<List<Operator>> remove(Collection<Player> players) {
        return ApiSupport.operatorList(raw, BASE + "/remove", ApiSupport.copy(players, "players"));
    }

}
