package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Message sent by the server to all or selected players.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SystemMessage(
        Message message,
        boolean overlay,
        List<Player> receivingPlayers
) {

    public SystemMessage {
        requireNonNull(message, "message");
        receivingPlayers = receivingPlayers == null
                ? List.of()
                : List.copyOf(receivingPlayers);
    }

    public static SystemMessage chat(Message message) {
        return new SystemMessage(message, false, List.of());
    }

    public static SystemMessage actionBar(Message message) {
        return new SystemMessage(message, true, List.of());
    }

    public SystemMessage to(Collection<Player> players) {
        return new SystemMessage(message, overlay, List.copyOf(requireNonNull(players, "players")));
    }

}
