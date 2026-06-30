package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Declared value type of a Minecraft game rule.
 *
 * <p>Modern MCSMP exposes game rules as either integer-valued or boolean-valued. The type returned by
 * {@link TypedGameRule#type()} is the best source for building editors, validating operator input, and deciding whether
 * to call {@link UntypedGameRule#integer(String, int)} or {@link UntypedGameRule#bool(String, boolean)}.</p>
 */
public enum GameRuleType {

    /**
     * Integer-valued game rule.
     */
    INTEGER("integer"),

    /**
     * Boolean-valued game rule.
     */
    BOOLEAN("boolean");

    private final String value;

    GameRuleType(String value) {
        this.value = value;
    }

    /**
     * Converts a protocol string to a game-rule type.
     *
     * @param value the lowercase protocol value
     *
     * @return the matching type
     *
     * @throws IllegalArgumentException if {@code value} is not a known game-rule type string
     */
    @JsonCreator
    public static GameRuleType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown game rule type: " + value));
    }

    /**
     * Returns the lowercase protocol value used in JSON.
     *
     * @return the MCSMP game-rule type string
     */
    @JsonValue
    public String value() {
        return value;
    }

}
