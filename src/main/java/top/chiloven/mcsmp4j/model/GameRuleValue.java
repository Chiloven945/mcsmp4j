package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;
import java.util.OptionalInt;

import static java.util.Objects.requireNonNull;

/**
 * Value of a MCSMP game rule.
 *
 * <p>Modern servers return booleans or integers. The string variant is retained for compatibility with
 * older snapshots and with server-side parsing of integer values from strings.</p>
 */
public sealed interface GameRuleValue permits
        GameRuleValue.BooleanValue,
        GameRuleValue.IntegerValue,
        GameRuleValue.StringValue {

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

    static GameRuleValue of(boolean value) {
        return new BooleanValue(value);
    }

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

    static GameRuleValue legacyString(String value) {
        return new StringValue(requireNonNull(value, "value"));
    }

    @JsonValue
    Object raw();

    default Optional<Boolean> asBoolean() {
        return this instanceof BooleanValue(boolean value)
                ? Optional.of(value)
                : Optional.empty();
    }

    default OptionalInt asInteger() {
        return this instanceof IntegerValue(int value)
                ? OptionalInt.of(value)
                : OptionalInt.empty();
    }

    default Optional<String> asString() {
        return this instanceof StringValue(String value)
                ? Optional.of(value)
                : Optional.empty();
    }

    record BooleanValue(boolean value) implements GameRuleValue {

        @Override
        public Boolean raw() {
            return value;
        }

    }

    record IntegerValue(int value) implements GameRuleValue {

        @Override
        public Integer raw() {
            return value;
        }

    }

    record StringValue(String value) implements GameRuleValue {

        public StringValue {
            requireNonNull(value, "value");
        }

        @Override
        public String raw() {
            return value;
        }

    }

}
