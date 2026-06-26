package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Minecraft version identifier exposed by the server status endpoint.
 *
 * @param name     the human-readable version name, such as {@code "1.21.11"} or a snapshot name
 * @param protocol the Minecraft network protocol number reported by the server
 */
public record MinecraftVersion(
        String name,
        int protocol
) {

    /**
     * Validates that the version name is present and non-blank.
     */
    public MinecraftVersion {
        requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

}
