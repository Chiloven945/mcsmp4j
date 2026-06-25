package top.chiloven.mcsmp4j.internal.api;

import tools.jackson.core.type.TypeReference;
import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.model.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

final class ApiSupport {

    static final TypeReference<List<Player>> PLAYER_LIST = new TypeReference<>() {
    };
    static final TypeReference<List<UserBan>> USER_BAN_LIST = new TypeReference<>() {
    };
    static final TypeReference<List<IpBan>> IP_BAN_LIST = new TypeReference<>() {
    };
    static final TypeReference<List<Operator>> OPERATOR_LIST = new TypeReference<>() {
    };
    static final TypeReference<List<TypedGameRule>> TYPED_GAME_RULE_LIST = new TypeReference<>() {
    };

    private ApiSupport() {
    }

    static List<String> copyStrings(Collection<String> values, String name) {
        var copy = copy(values, name);
        copy.stream()
                .filter(String::isBlank)
                .forEach(value -> {
                    throw new IllegalArgumentException(name + " must not contain blank strings");
                });
        return copy;
    }

    static <T> List<T> copy(Collection<T> values, String name) {
        requireNonNull(values, name);
        return List.copyOf(values);
    }

    static int nonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
        return value;
    }

    static int positive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return value;
    }

    static String nonBlank(String value, String name) {
        requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    static CompletableFuture<List<Player>> playerList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, PLAYER_LIST, params);
    }

    static CompletableFuture<List<UserBan>> userBanList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, USER_BAN_LIST, params);
    }

    static CompletableFuture<List<IpBan>> ipBanList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, IP_BAN_LIST, params);
    }

    static CompletableFuture<List<Operator>> operatorList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, OPERATOR_LIST, params);
    }

    static CompletableFuture<List<TypedGameRule>> typedGameRuleList(
            RawApi raw,
            String method,
            Object... params
    ) {
        return raw.call(method, TYPED_GAME_RULE_LIST, params);
    }

}
