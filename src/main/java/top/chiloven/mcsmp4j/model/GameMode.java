package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Minecraft game mode.
 */
public enum GameMode {

    SURVIVAL("survival"),
    CREATIVE("creative"),
    ADVENTURE("adventure"),
    SPECTATOR("spectator");

    private final String value;

    GameMode(String value) {
        this.value = value;
    }

    @JsonCreator
    public static GameMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(mode -> mode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown game mode: " + value));
    }

    @JsonValue
    public String value() {
        return value;
    }

}
