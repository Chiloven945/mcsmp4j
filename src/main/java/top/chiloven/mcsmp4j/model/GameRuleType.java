package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * MCSMP game rule value type.
 */
public enum GameRuleType {

    INTEGER("integer"),
    BOOLEAN("boolean");

    private final String value;

    GameRuleType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static GameRuleType fromValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown game rule type: " + value));
    }

    @JsonValue
    public String value() {
        return value;
    }

}
