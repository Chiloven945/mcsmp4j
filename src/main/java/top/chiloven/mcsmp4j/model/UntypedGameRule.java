package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Game rule update request. The server infers the target game rule type from the key.
 */
public record UntypedGameRule(
        String key,
        GameRuleValue value
) {

    public UntypedGameRule {
        requireNonNull(key, "key");
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        requireNonNull(value, "value");
    }

    public static UntypedGameRule bool(String key, boolean value) {
        return new UntypedGameRule(key, GameRuleValue.of(value));
    }

    public static UntypedGameRule integer(String key, int value) {
        return new UntypedGameRule(key, GameRuleValue.of(value));
    }

    public static UntypedGameRule legacyString(String key, String value) {
        return new UntypedGameRule(key, GameRuleValue.legacyString(value));
    }

}
