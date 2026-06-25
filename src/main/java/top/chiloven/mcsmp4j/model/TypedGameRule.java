package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Game rule returned by the server together with its declared value type.
 */
public record TypedGameRule(
        String key,
        GameRuleType type,
        GameRuleValue value
) {

    public TypedGameRule {
        requireNonNull(key, "key");
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        requireNonNull(type, "type");
        requireNonNull(value, "value");
    }

}
