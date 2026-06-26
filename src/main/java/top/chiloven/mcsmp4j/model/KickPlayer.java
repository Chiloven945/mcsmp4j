package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Request payload for kicking a player.
 *
 * <p>The payload identifies the target player and may include a message shown to the player when the server
 * disconnects them.</p>
 *
 * @param player  the player to kick
 * @param message optional disconnect message shown to the player
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
