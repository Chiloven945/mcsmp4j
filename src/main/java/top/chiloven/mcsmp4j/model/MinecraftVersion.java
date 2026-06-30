package top.chiloven.mcsmp4j.model;

import static java.util.Objects.requireNonNull;

/**
 * Minecraft version metadata reported by the server.
 *
 * <p>This record is display-oriented. It describes the Minecraft server software version, not the mcsmp4j artifact
 * version
 * and not necessarily the MCSMP protocol version. Use {@link top.chiloven.mcsmp4j.version.McsmpProtocolVersion} and
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities} for protocol compatibility decisions.</p>
 *
 * @param name     the human-readable Minecraft version name, such as {@code 1.21.11}
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
