package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * User ban entry accepted and returned by the MCSMP user ban endpoints.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserBan(
        Player player,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

    public UserBan {
        requireNonNull(player, "player");
        if (reason != null && reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        if (source != null && source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
    }

    public static UserBan permanent(Player player) {
        return new UserBan(player, null, null, null);
    }

    public static UserBan permanent(Player player, String reason) {
        return new UserBan(player, requireNonNull(reason, "reason"), null, null);
    }

    public static UserBan byName(String name) {
        return permanent(Player.byName(name));
    }

    public static UserBan temporary(Player player, Instant expires) {
        return new UserBan(player, null, null, requireNonNull(expires, "expires"));
    }

    public boolean hasExpiration() {
        return expires != null;
    }

}
