package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Request payload for kicking a player, optionally with a displayed reason.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record KickPlayer(
        Player player,
        @Nullable Message message
) {

    public KickPlayer {
        requireNonNull(player, "player");
    }

    public static KickPlayer byName(String name) {
        return of(Player.byName(name));
    }

    public static KickPlayer of(Player player) {
        return new KickPlayer(player, null);
    }

    public static KickPlayer byName(String name, String reason) {
        return withMessage(Player.byName(name), Message.literal(reason));
    }

    public static KickPlayer withMessage(Player player, Message message) {
        return new KickPlayer(player, requireNonNull(message, "message"));
    }

}
