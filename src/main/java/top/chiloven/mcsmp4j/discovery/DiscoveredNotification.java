package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Notification entry advertised by {@code rpc.discover}.
 */
public record DiscoveredNotification(
        String name,
        @Nullable JsonNode schema
) {

    public DiscoveredNotification {
        requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

}
