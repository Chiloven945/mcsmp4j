package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Notification entry advertised by {@code rpc.discover}.
 *
 * <p>Notifications are server-to-client JSON-RPC messages that do not have an {@code id}. This record stores the
 * notification method name and any raw schema details returned by the server so applications can decide whether to
 * register typed or raw listeners.</p>
 *
 * @param name   the full notification method name, such as {@code minecraft:notification/players/joined}
 * @param schema optional raw schema information associated with the notification
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
