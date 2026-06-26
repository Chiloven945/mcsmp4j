package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Minecraft game mode values used by MCSMP server-settings methods.
 *
 * <p>The JSON representation is the lowercase protocol value, for example {@code "survival"}. These values are
 * used for the server's default game mode and for force-game-mode related settings.</p>
 */
public enum GameMode {

    /**
     * Survival mode.
     */
    SURVIVAL("survival"),

    /**
     * Creative mode.
     */
    CREATIVE("creative"),

    /**
     * Adventure mode.
     */
    ADVENTURE("adventure"),

    /**
     * Spectator mode.
     */
    SPECTATOR("spectator");

    private final String value;

    GameMode(String value) {
        this.value = value;
    }

    /**
     * Converts a protocol string to a game mode enum constant.
     *
     * @param value the lowercase protocol value
     *
     * @return the matching game mode
     *
     * @throws IllegalArgumentException if {@code value} is not a known game mode string
     */
    @JsonCreator
    public static GameMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(mode -> mode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown game mode: " + value));
    }

    /**
     * Returns the lowercase protocol value used in JSON.
     *
     * @return the MCSMP game mode string
     */
    @JsonValue
    public String value() {
        return value;
    }

}
