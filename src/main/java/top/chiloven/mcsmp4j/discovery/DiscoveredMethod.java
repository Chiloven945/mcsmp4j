package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Description of one JSON-RPC method advertised by {@code rpc.discover}.
 *
 * <p>The record preserves the method name and the optional raw schema fragment associated with that method. The schema
 * is
 * kept as a Jackson {@link tools.jackson.databind.JsonNode} so tools can inspect details that mcsmp4j does not yet
 * model, such as extension parameters, result schemas, descriptions, or custom metadata.</p>
 *
 * @param name   the full JSON-RPC method name, such as {@code minecraft:server/status}
 * @param schema the raw discovery schema for the method, or {@code null} when no schema fragment was present
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
