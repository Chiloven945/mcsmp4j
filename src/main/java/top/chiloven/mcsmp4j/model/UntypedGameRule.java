package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Game rule update request.
 *
 * <p>The update endpoint accepts a key and scalar value. The server infers the expected value type from the game
 * rule key and returns a {@link TypedGameRule} after applying the update.</p>
 *
 * @param key   the game-rule key to update
 * @param value the new value to send
 */
public record UntypedGameRule(
        String key,
        GameRuleValue value
) {

    /**
     * Validates that the key and value are present.
     */
    public UntypedGameRule {
        requireNonNull(key, "key");
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must not be blank");
        }
        requireNonNull(value, "value");
    }

    /**
     * Creates a boolean game-rule update request.
     *
     * @param key   the game-rule key
     * @param value the new boolean value
     *
     * @return an update request
     */
    public static UntypedGameRule bool(String key, boolean value) {
        return new UntypedGameRule(key, GameRuleValue.of(value));
    }

    /**
     * Creates an integer game-rule update request.
     *
     * @param key   the game-rule key
     * @param value the new integer value
     *
     * @return an update request
     */
    public static UntypedGameRule integer(String key, int value) {
        return new UntypedGameRule(key, GameRuleValue.of(value));
    }

    /**
     * Creates a legacy string game-rule update request.
     *
     * <p>This is intended for compatibility with older experimental protocol versions or custom servers that
     * still expect string values.</p>
     *
     * @param key   the game-rule key
     * @param value the new string value
     *
     * @return an update request
     */
    public static UntypedGameRule legacyString(String key, String value) {
        return new UntypedGameRule(key, GameRuleValue.legacyString(value));
    }

}
