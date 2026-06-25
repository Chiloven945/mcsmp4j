package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Minecraft version identifier exposed by the server status endpoint.
 */
public record MinecraftVersion(
        String name,
        int protocol
) {

    public MinecraftVersion {
        requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

}
