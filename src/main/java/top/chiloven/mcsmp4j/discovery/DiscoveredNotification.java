package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Description of one JSON-RPC notification advertised by {@code rpc.discover}.
 *
 * <p>Notifications are server-to-client messages without a JSON-RPC id. This record is used when building capability
 * snapshots and by diagnostic tools that want to list exactly which event methods the server claims to emit.</p>
 *
 * @param name   the full notification method name, such as {@code minecraft:notification/players/joined}
 * @param schema the raw discovery schema for the notification, or {@code null} when no schema fragment was present
 */
public record DiscoveredNotification(
        String name,
        @Nullable JsonNode schema
) {

    /**
     * Validates that the discovered notification name is present and non-blank.
     */
    public DiscoveredNotification {
        requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

}
