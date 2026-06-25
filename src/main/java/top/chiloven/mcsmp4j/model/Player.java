package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Player specifier accepted by MCSMP.
 *
 * <p>Either {@code id} or {@code name} must be present. If both are present, vanilla servers use the
 * UUID first and fall back to the name only when the UUID is absent.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Player(
        @Nullable UUID id,
        @Nullable String name
) {

    public Player {
        if (id == null && (name == null || name.isBlank())) {
            throw new IllegalArgumentException("Either id or name must be provided");
        }
    }

    public static Player byId(UUID id) {
        return new Player(requireNonNull(id, "id"), null);
    }

    public static Player byName(String name) {
        return new Player(null, requireNonNull(name, "name"));
    }

    public static Player of(UUID id, String name) {
        return new Player(requireNonNull(id, "id"), requireNonNull(name, "name"));
    }

    public boolean hasId() {
        return id != null;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public String displayName() {
        if (name != null && !name.isBlank()) {
            return name;
        }
        return Objects.toString(id);
    }

}
