package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;
import java.util.OptionalInt;

import static java.util.Objects.requireNonNull;

/**
 * Value of a Minecraft game rule.
 *
 * <p>Protocol version {@code 2.0.0} changed game-rule values from strings to typed JSON booleans and integers.
 * This sealed interface therefore supports all three shapes: {@link BooleanValue}, {@link IntegerValue}, and the legacy
 * {@link StringValue}. The typed API preserves the legacy variant so clients can still interoperate with older
 * experimental servers and custom implementations.</p>
 */
public sealed interface GameRuleValue permits
        GameRuleValue.BooleanValue,
        GameRuleValue.IntegerValue,
        GameRuleValue.StringValue {

    /**
     * Decodes a game-rule value from a scalar JSON value.
     *
     * <p>Boolean values become {@link BooleanValue}; integer-like numeric values become {@link IntegerValue};
     * strings are parsed as integers when possible and otherwise retained as {@link StringValue} for legacy
     * compatibility.</p>
     *
     * @param value the raw scalar value supplied by Jackson
     *
     * @return the corresponding game-rule value wrapper
     *
     * @throws IllegalArgumentException if the value is {@code null}, outside the integer range, or not a supported
     *                                  scalar type
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    static GameRuleValue fromJson(Object value) {
        return switch (value) {
            case Boolean booleanValue -> of(booleanValue);
            case Byte byteValue -> of(byteValue.intValue());
            case Short shortValue -> of(shortValue.intValue());
            case Integer integerValue -> of(integerValue);
            case Long longValue -> {
                if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("integer game rule value is out of int range: " + longValue);
                }
                yield of(longValue.intValue());
            }
            case String stringValue -> parseString(stringValue);
            case null -> throw new IllegalArgumentException("game rule value must not be null");
            default -> throw new IllegalArgumentException("Unsupported game rule value type: " + value.getClass()
                    .getName());
        };
    }

    /**
     * Creates a boolean game-rule value.
     *
     * @param value the boolean value
     *
     * @return a boolean game-rule value wrapper
     */
    static GameRuleValue of(boolean value) {
        return new BooleanValue(value);
    }

    /**
     * Creates an integer game-rule value.
     *
     * @param value the integer value
     *
     * @return an integer game-rule value wrapper
     */
    static GameRuleValue of(int value) {
        return new IntegerValue(value);
    }

    private static GameRuleValue parseString(String value) {
        requireNonNull(value, "value");
        try {
            return of(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
            return legacyString(value);
        }
    }

    /**
     * Creates a legacy string game-rule value.
     *
     * @param value the legacy string value
     *
     * @return a string game-rule value wrapper
     */
    static GameRuleValue legacyString(String value) {
        return new StringValue(requireNonNull(value, "value"));
    }

    /**
     * Returns the scalar value that should be written to JSON.
     *
     * @return a {@link Boolean}, {@link Integer}, or {@link String}
     */
    @JsonValue
    Object raw();

    /**
     * Returns this value as a boolean when it is a boolean value.
     *
     * @return an optional containing the boolean value, or empty for non-boolean variants
     */
    default Optional<Boolean> asBoolean() {
        return this instanceof BooleanValue(boolean value)
                ? Optional.of(value)
                : Optional.empty();
    }

    /**
     * Returns this value as an integer when it is an integer value.
     *
     * @return an optional integer containing the value, or empty for non-integer variants
     */
    default OptionalInt asInteger() {
        return this instanceof IntegerValue(int value)
                ? OptionalInt.of(value)
                : OptionalInt.empty();
    }

    /**
     * Returns this value as a legacy string when it is a string value.
     *
     * @return an optional containing the string value, or empty for non-string variants
     */
    default Optional<String> asString() {
        return this instanceof StringValue(String value)
                ? Optional.of(value)
                : Optional.empty();
    }

    /**
     * Boolean game-rule value.
     *
     * @param value the boolean scalar value
     */
    record BooleanValue(boolean value) implements GameRuleValue {

        /**
         * Returns the scalar value used for JSON serialization.
         *
         * @return the boxed boolean value
         */
        @Override
        public Boolean raw() {
            return value;
        }

    }

    /**
     * Integer game-rule value.
     *
     * @param value the integer scalar value
     */
    record IntegerValue(int value) implements GameRuleValue {

        /**
         * Returns the scalar value used for JSON serialization.
         *
         * @return the boxed integer value
         */
        @Override
        public Integer raw() {
            return value;
        }

    }

    /**
     * Legacy string game-rule value.
     *
     * @param value the non-null string scalar value
     */
    record StringValue(String value) implements GameRuleValue {

        /**
         * Validates that the legacy string value is non-null.
         */
        public StringValue {
            requireNonNull(value, "value");
        }

        /**
         * Returns the scalar value used for JSON serialization.
         *
         * @return the string value
         */
        @Override
        public String raw() {
            return value;
        }

    }

}
