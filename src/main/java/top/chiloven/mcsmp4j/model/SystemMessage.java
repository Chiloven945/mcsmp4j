package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Payload sent through {@link top.chiloven.mcsmp4j.api.ServerApi#systemMessage(SystemMessage)}.
 *
 * <p>A system message contains the message content, a display-mode flag, and an optional recipient list. When
 * {@link #overlay()} is {@code false}, the message is delivered as a normal chat/system message. When it is
 * {@code true}, the server should display the message as an overlay/action-bar style message where supported. An empty
 * recipient list means the server should broadcast to all applicable players.</p>
 *
 * <h2>Targeting</h2>
 *
 * <p>Use {@link #chat(Message)} or {@link #actionBar(Message)} to create a broadcast, then call
 * {@link #to(java.util.Collection)}
 * when the message should be sent only to selected players. Recipient selectors follow the same rules as
 * {@link Player}; the server decides whether partial selectors match current players.</p>
 *
 * @param message          the text or translatable message content
 * @param overlay          whether the message should be displayed as an overlay/action-bar message
 * @param receivingPlayers explicit target players; an empty list means broadcast
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
