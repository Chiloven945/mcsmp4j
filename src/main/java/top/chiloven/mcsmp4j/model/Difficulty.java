package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Minecraft server difficulty.
 */
public enum Difficulty {

    PEACEFUL("peaceful"),
    EASY("easy"),
    NORMAL("normal"),
    HARD("hard");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Difficulty fromValue(String value) {
        return Arrays.stream(values())
                .filter(difficulty -> difficulty.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown difficulty: " + value));
    }

    @JsonValue
    public String value() {
        return value;
    }

}
