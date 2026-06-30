package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Operator-list entry used by the {@code minecraft:operators} API.
 *
 * <p>An operator entry grants elevated Minecraft permissions to a player. The {@link #permissionLevel()} value is the
 * permission
 * level used by the server for command authorization, and {@link #bypassesPlayerLimit()} indicates whether the player
 * can join when the server is otherwise full. Management applications should treat this record as security-sensitive
 * because adding or changing operators can allow in-game administrative control.</p>
 *
 * <p>The {@link #player()} component accepts the same partial selector rules as {@link Player}. When the server
 * returns
 * operator entries it may include resolved UUIDs and names even if a request supplied only one field.</p>
 *
 * @param player              the player who is or should become an operator
 * @param permissionLevel     the Minecraft operator permission level requested or reported by the server, or
 *                            {@code null} for the server default
 * @param bypassesPlayerLimit whether the operator may bypass the configured maximum player count
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Operator(
        Player player,
        @Nullable Integer permissionLevel,
        @Nullable Boolean bypassesPlayerLimit
) {

    /**
     * Validates the player and optional permission level.
     */
    public Operator {
        requireNonNull(player, "player");
        if (permissionLevel != null && (permissionLevel < 0 || permissionLevel > 4)) {
            throw new IllegalArgumentException("permissionLevel must be between 0 and 4");
        }
    }

    /**
     * Creates an operator entry using server default options.
     *
     * @param player the player to make an operator
     *
     * @return an operator entry with no explicit permission level or bypass flag
     */
    public static Operator of(
            Player player
    ) {
        return new Operator(player, null, null);
    }

    /**
     * Creates an operator entry with an explicit permission level.
     *
     * @param player          the player to make an operator
     * @param permissionLevel the permission level to request
     *
     * @return an operator entry with the supplied permission level
     */
    public static Operator of(
            Player player,
            int permissionLevel
    ) {
        return new Operator(player, permissionLevel, null);
    }

    /**
     * Creates an operator entry with explicit permission and player-limit options.
     *
     * @param player              the player to make an operator
     * @param permissionLevel     the permission level to request
     * @param bypassesPlayerLimit whether the operator should bypass the configured player limit
     *
     * @return an operator entry with all options set
     */
    public static Operator of(
            Player player,
            int permissionLevel,
            boolean bypassesPlayerLimit
    ) {
        return new Operator(player, permissionLevel, bypassesPlayerLimit);
    }

}
