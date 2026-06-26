package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Game rule returned by the server together with its declared value type.
 *
 * <p>Typed game rules are produced by {@link top.chiloven.mcsmp4j.api.GamerulesApi#list()} and returned after
 * updates. The {@link #type()} value describes the server's declared type, while {@link #value()} contains the current
 * scalar value.</p>
 *
 * @param key   the game-rule key, such as {@code doDaylightCycle}
 * @param type  the declared value type
 * @param value the current value
 */
public record TypedGameRule(
        String key,
        GameRuleType type,
        GameRuleValue value
) {

    /**
     * Validates that the key, type, and value are present.
     */
    public TypedGameRule {
        requireNonNull(key, "key");
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        requireNonNull(type, "type");
        requireNonNull(value, "value");
    }

}
