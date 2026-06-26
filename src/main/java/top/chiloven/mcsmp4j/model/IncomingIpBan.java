package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Request payload for creating an IP ban.
 *
 * <p>At least one of {@code ip} or {@code player} must be present. A request can ban a concrete address directly
 * or ask the server to resolve a player's current address. Optional reason, source, and expiration metadata can be
 * attached to the ban entry.</p>
 *
 * @param ip      optional IP address to ban
 * @param player  optional player whose address should be banned
 * @param reason  optional human-readable ban reason
 * @param source  optional source or actor that created the ban
 * @param expires optional expiration instant; {@code null} means permanent
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IncomingIpBan(
        @Nullable String ip,
        @Nullable Player player,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

    /**
     * Validates that the request identifies either an IP address or a player and that optional text fields are
     * non-blank.
     */
    public IncomingIpBan {
        if ((ip == null || ip.isBlank()) && player == null) {
            throw new IllegalArgumentException("Either ip or player must be provided");
        }
        if (reason != null && reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        if (source != null && source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
    }

    /**
     * Creates an IP-ban request for a concrete IP address.
     *
     * @param ip the IP address to ban
     *
     * @return a request that bans {@code ip}
     */
    public static IncomingIpBan byIp(String ip) {
        return new IncomingIpBan(requireNonNull(ip, "ip"), null, null, null, null);
    }

    /**
     * Creates an IP-ban request by player name.
     *
     * @param name the player name whose current address should be banned
     *
     * @return a request that asks the server to resolve the player's address
     */
    public static IncomingIpBan byPlayerName(String name) {
        return byPlayer(Player.byName(name));
    }

    /**
     * Creates an IP-ban request by player specifier.
     *
     * @param player the player whose current address should be banned
     *
     * @return a request that asks the server to resolve the player's address
     */
    public static IncomingIpBan byPlayer(Player player) {
        return new IncomingIpBan(null, requireNonNull(player, "player"), null, null, null);
    }

    /**
     * Returns a copy with a ban reason.
     *
     * @param reason the non-null reason to attach
     *
     * @return a copy of this request with {@code reason}
     */
    public IncomingIpBan withReason(String reason) {
        return new IncomingIpBan(ip, player, requireNonNull(reason, "reason"), source, expires);
    }

    /**
     * Returns a copy with a source string.
     *
     * @param source the non-null source or actor string to attach
     *
     * @return a copy of this request with {@code source}
     */
    public IncomingIpBan withSource(String source) {
        return new IncomingIpBan(ip, player, reason, requireNonNull(source, "source"), expires);
    }

    /**
     * Returns a copy that expires at the supplied instant.
     *
     * @param expires the expiration instant
     *
     * @return a copy of this request with an expiration
     */
    public IncomingIpBan expiringAt(Instant expires) {
        return new IncomingIpBan(ip, player, reason, source, requireNonNull(expires, "expires"));
    }

}
