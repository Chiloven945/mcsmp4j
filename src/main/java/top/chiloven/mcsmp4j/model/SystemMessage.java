package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * System message payload sent by the server to all or selected players.
 *
 * <p>The {@code overlay} flag controls whether the message is displayed as a normal chat/system message or as an
 * action-bar overlay. When {@code receivingPlayers} is empty, the server sends the message to all applicable
 * players.</p>
 *
 * @param message          the message content
 * @param overlay          {@code true} for action-bar overlay delivery, {@code false} for normal chat/system delivery
 * @param receivingPlayers optional target players; empty means all players
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SystemMessage(
        Message message,
        boolean overlay,
        List<Player> receivingPlayers
) {

    /**
     * Validates the message and defensively copies recipient players.
     */
    public SystemMessage {
        requireNonNull(message, "message");
        receivingPlayers = receivingPlayers == null
                ? List.of()
                : List.copyOf(receivingPlayers);
    }

    /**
     * Creates a normal chat/system message addressed to all players.
     *
     * @param message the message to send
     *
     * @return a non-overlay system message
     */
    public static SystemMessage chat(Message message) {
        return new SystemMessage(message, false, List.of());
    }

    /**
     * Creates an action-bar overlay message addressed to all players.
     *
     * @param message the message to send
     *
     * @return an overlay system message
     */
    public static SystemMessage actionBar(Message message) {
        return new SystemMessage(message, true, List.of());
    }

    /**
     * Returns a copy targeted to the supplied players.
     *
     * @param players the players that should receive the message
     *
     * @return a copy of this message with explicit recipients
     */
    public SystemMessage to(Collection<Player> players) {
        return new SystemMessage(message, overlay, List.copyOf(requireNonNull(players, "players")));
    }

}
