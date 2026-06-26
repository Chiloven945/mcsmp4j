package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Player specifier accepted by MCSMP requests and returned by server responses.
 *
 * <p>A player may be identified by UUID, by name, or by both. Request payloads often accept partial players so
 * clients can address a player by the information they have available. Response payloads commonly include both fields.
 * This record enforces that at least one of {@code id} or {@code name} is present.</p>
 *
 * @param id   the player's UUID, or {@code null} when only a name is available
 * @param name the player's current or requested name, or {@code null} when only a UUID is available
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Player(
        @Nullable UUID id,
        @Nullable String name
) {

    /**
     * Validates that either UUID or name is supplied.
     */
    public Player {
        if (id == null && (name == null || name.isBlank())) {
            throw new IllegalArgumentException("Either id or name must be provided");
        }
    }

    /**
     * Creates a player specifier by UUID.
     *
     * @param id the player's UUID
     *
     * @return a player specifier containing only the UUID
     */
    public static Player byId(UUID id) {
        return new Player(requireNonNull(id, "id"), null);
    }

    /**
     * Creates a player specifier by name.
     *
     * @param name the non-blank player name
     *
     * @return a player specifier containing only the name
     */
    public static Player byName(String name) {
        return new Player(null, requireNonNull(name, "name"));
    }

    /**
     * Creates a player specifier containing both UUID and name.
     *
     * @param id   the player's UUID
     * @param name the player's name
     *
     * @return a player specifier containing both fields
     */
    public static Player of(UUID id, String name) {
        return new Player(requireNonNull(id, "id"), requireNonNull(name, "name"));
    }

    /**
     * Returns whether this player contains a UUID.
     *
     * @return {@code true} when {@link #id()} is non-null
     */
    public boolean hasId() {
        return id != null;
    }

    /**
     * Returns whether this player contains a usable name.
     *
     * @return {@code true} when {@link #name()} is non-null and not blank
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /**
     * Returns a display-friendly identifier for logs and user interfaces.
     *
     * <p>The player name is preferred when present; otherwise the UUID string is returned.</p>
     *
     * @return the name or UUID string for this player
     */
    public String displayName() {
        if (name != null && !name.isBlank()) {
            return name;
        }
        return Objects.toString(id);
    }

}
