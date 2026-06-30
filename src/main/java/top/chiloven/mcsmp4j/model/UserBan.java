package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * User-ban entry used by the {@code minecraft:bans} API.
 *
 * <p>A user ban targets a Minecraft player identity rather than a network address. The server stores the banned player
 * and
 * may also store the reason, source, and optional expiration instant. A {@code null} expiration means the ban is
 * permanent. The server is responsible for enforcing the ban during login and for deciding how expired bans are
 * handled.</p>
 *
 * <h2>Creation helpers</h2>
 *
 * <p>Use {@link #permanent(Player)}, {@link #permanent(Player, String)}, {@link #byName(String)}, or
 * {@link #temporary(Player, java.time.Instant)} for common cases. The canonical constructor remains available when an
 * application needs to specify source or all optional fields explicitly.</p>
 *
 * @param player  the banned player selector or identity
 * @param reason  optional human-readable reason; {@code null} means no reason was supplied
 * @param source  optional source/actor that created the ban; {@code null} means unspecified
 * @param expires optional expiration instant; {@code null} means permanent
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserBan(
        Player player,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

    /**
     * Validates the player and optional text fields.
     */
    public UserBan {
        requireNonNull(player, "player");
        if (reason != null && reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        if (source != null && source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
    }

    /**
     * Creates a permanent user ban with a reason.
     *
     * @param player the player to ban
     * @param reason the ban reason
     *
     * @return a permanent user ban entry with a reason
     */
    public static UserBan permanent(Player player, String reason) {
        return new UserBan(player, requireNonNull(reason, "reason"), null, null);
    }

    /**
     * Creates a permanent user ban for a player name.
     *
     * @param name the player name to ban
     *
     * @return a permanent user ban entry
     */
    public static UserBan byName(String name) {
        return permanent(Player.byName(name));
    }

    /**
     * Creates a permanent user ban with no reason.
     *
     * @param player the player to ban
     *
     * @return a permanent user ban entry
     */
    public static UserBan permanent(Player player) {
        return new UserBan(player, null, null, null);
    }

    /**
     * Creates a temporary user ban.
     *
     * @param player  the player to ban
     * @param expires the expiration instant
     *
     * @return a temporary user ban entry
     */
    public static UserBan temporary(Player player, Instant expires) {
        return new UserBan(player, null, null, requireNonNull(expires, "expires"));
    }

    /**
     * Returns whether this ban has an expiration instant.
     *
     * @return {@code true} for a temporary ban
     */
    public boolean hasExpiration() {
        return expires != null;
    }

}
