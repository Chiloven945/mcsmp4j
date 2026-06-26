package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Method entry advertised by {@code rpc.discover}.
 *
 * <p>Discovery schemas are intentionally kept close to their original JSON form. mcsmp4j extracts method names
 * for feature checks while retaining the optional raw schema node so advanced applications can inspect parameters,
 * results, descriptions, or extension metadata that the library does not model directly.</p>
 *
 * @param name   the full JSON-RPC method name, such as {@code minecraft:server/status}
 * @param schema optional raw schema information associated with the method
 */
public record DiscoveredMethod(
        String name,
        @Nullable JsonNode schema
) {

    /**
     * Validates that the discovered method name is present and non-blank.
     */
    public DiscoveredMethod {
        requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

}
