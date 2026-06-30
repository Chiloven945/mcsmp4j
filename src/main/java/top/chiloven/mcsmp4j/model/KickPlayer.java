package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Request object used to kick a connected player.
 *
 * <p>The kick endpoint accepts a player selector and an optional message. The selector identifies the connected player
 * or
 * players the server should remove. The optional {@link #message()} is presented to the player by the server, typically
 * as the disconnect reason. If no message is provided, the server chooses its default reason.</p>
 *
 * <p>Kick operations are best treated as administrative side effects rather than ordinary state changes. A successful
 * response indicates which players the server reports as kicked, but clients should still listen for
 * {@link top.chiloven.mcsmp4j.event.PlayerLeftEvent} when maintaining a live player list.</p>
 *
 * @param player  the player selector to kick
 * @param message optional disconnect message, or {@code null} to let the server choose a default
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record KickPlayer(
        Player player,
        @Nullable Message message
) {

    /**
     * Validates that the target player is present.
     */
    public KickPlayer {
        requireNonNull(player, "player");
    }

    /**
     * Creates a kick request for a player name without a custom message.
     *
     * @param name the player name to kick
     *
     * @return a kick request targeting the named player
     */
    public static KickPlayer byName(String name) {
        return of(Player.byName(name));
    }

    /**
     * Creates a kick request without a custom message.
     *
     * @param player the player to kick
     *
     * @return a kick request targeting {@code player}
     */
    public static KickPlayer of(Player player) {
        return new KickPlayer(player, null);
    }

    /**
     * Creates a kick request for a player name with a literal reason.
     *
     * @param name   the player name to kick
     * @param reason the literal disconnect reason
     *
     * @return a kick request targeting the named player with a message
     */
    public static KickPlayer byName(String name, String reason) {
        return withMessage(Player.byName(name), Message.literal(reason));
    }

    /**
     * Creates a kick request with an explicit message object.
     *
     * @param player  the player to kick
     * @param message the disconnect message to send
     *
     * @return a kick request targeting {@code player} with {@code message}
     */
    public static KickPlayer withMessage(Player player, Message message) {
        return new KickPlayer(player, requireNonNull(message, "message"));
    }

}
