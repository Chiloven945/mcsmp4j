package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Server operator entry.
 *
 * <p>Operators are players that have elevated command permissions. The permission level and player-limit bypass
 * flag are optional in request payloads so callers can let the server apply its defaults.</p>
 *
 * @param player              the player that should be or is an operator
 * @param permissionLevel     optional command permission level, usually between {@code 0} and {@code 4}
 * @param bypassesPlayerLimit optional flag indicating whether this operator bypasses the player limit
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
