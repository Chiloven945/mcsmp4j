package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Game-rule state returned by the server with its declared type.
 *
 * <p>A typed game rule contains the rule key, the server-declared value type, and the current scalar value. It is
 * returned
 * by {@link top.chiloven.mcsmp4j.api.GamerulesApi#list()} and by successful update calls. The type tells applications
 * how to render editors and validate future changes; the value holds the current state.</p>
 *
 * <p>When interoperating with older experimental protocol versions or custom servers, the value may be a legacy string
 * variant even if the type is known. Use {@link GameRuleValue#asBoolean()}, {@link GameRuleValue#asInteger()}, and
 * {@link GameRuleValue#asString()} when building generic UI.</p>
 *
 * @param key   the game-rule key, such as {@code doDaylightCycle}
 * @param type  the declared value type reported by the server
 * @param value the current scalar value
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
