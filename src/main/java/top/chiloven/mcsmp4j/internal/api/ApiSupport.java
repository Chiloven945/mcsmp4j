package top.chiloven.mcsmp4j.internal.api;

import tools.jackson.core.type.TypeReference;
import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.model.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

final class ApiSupport {

    static final TypeReference<List<Player>> PLAYER_LIST = new TypeReference<>() {
    };

    private ApiSupport() {
    }

    static <T> List<T> copy(Collection<T> values, String name) {
        requireNonNull(values, name);
        return List.copyOf(values);
    }

    static CompletableFuture<List<Player>> playerList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, PLAYER_LIST, params);
    }

}
