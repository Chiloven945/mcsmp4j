package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Server operator entry.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Operator(
        Player player,
        @Nullable Integer permissionLevel,
        @Nullable Boolean bypassesPlayerLimit
) {

    public Operator {
        requireNonNull(player, "player");
        if (permissionLevel != null && (permissionLevel < 0 || permissionLevel > 4)) {
            throw new IllegalArgumentException("permissionLevel must be between 0 and 4");
        }
    }

    public static Operator of(
            Player player
    ) {
        return new Operator(player, null, null);
    }

    public static Operator of(
            Player player,
            int permissionLevel
    ) {
        return new Operator(player, permissionLevel, null);
    }

    public static Operator of(
            Player player,
            int permissionLevel,
            boolean bypassesPlayerLimit
    ) {
        return new Operator(player, permissionLevel, bypassesPlayerLimit);
    }

}
