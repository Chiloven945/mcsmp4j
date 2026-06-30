package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Minecraft server difficulty values used by server settings.
 *
 * <p>The enum constants serialize to the lowercase protocol strings expected by MCSMP: {@code peaceful}, {@code easy},
 * {@code normal}, and {@code hard}. Use {@link top.chiloven.mcsmp4j.api.ServerSettingsApi#difficulty()} to read the
 * current server difficulty and {@link top.chiloven.mcsmp4j.api.ServerSettingsApi#setDifficulty(Difficulty)} to request
 * a change.</p>
 *
 * <p>Changing difficulty can have immediate gameplay effects. Management applications should surface this operation as
 * a server setting change, not as a cosmetic preference.</p>
 */
public enum Difficulty {

    /**
     * Peaceful difficulty.
     */
    PEACEFUL("peaceful"),

    /**
     * Easy difficulty.
     */
    EASY("easy"),

    /**
     * Normal difficulty.
     */
    NORMAL("normal"),

    /**
     * Hard difficulty.
     */
    HARD("hard");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }

    /**
     * Converts a protocol string to a difficulty enum constant.
     *
     * @param value the lowercase protocol value
     *
     * @return the matching difficulty
     *
     * @throws IllegalArgumentException if {@code value} is not a known difficulty string
     */
    @JsonCreator
    public static Difficulty fromValue(String value) {
        return Arrays.stream(values())
                .filter(difficulty -> difficulty.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown difficulty: " + value));
    }

    /**
     * Returns the lowercase protocol value used in JSON.
     *
     * @return the MCSMP difficulty string
     */
    @JsonValue
    public String value() {
        return value;
    }

}
