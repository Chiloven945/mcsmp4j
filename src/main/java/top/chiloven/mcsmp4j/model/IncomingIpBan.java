package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Request payload for creating an IP ban.
 *
 * <p>At least one of {@code ip} or {@code player} must be present. Vanilla servers prefer a valid
 * {@code ip} and fall back to the player's current IP when the IP is absent or invalid.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IncomingIpBan(
        @Nullable String ip,
        @Nullable Player player,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

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

    public static IncomingIpBan byIp(String ip) {
        return new IncomingIpBan(requireNonNull(ip, "ip"), null, null, null, null);
    }

    public static IncomingIpBan byPlayerName(String name) {
        return byPlayer(Player.byName(name));
    }

    public static IncomingIpBan byPlayer(Player player) {
        return new IncomingIpBan(null, requireNonNull(player, "player"), null, null, null);
    }

    public IncomingIpBan withReason(String reason) {
        return new IncomingIpBan(ip, player, requireNonNull(reason, "reason"), source, expires);
    }

    public IncomingIpBan withSource(String source) {
        return new IncomingIpBan(ip, player, reason, requireNonNull(source, "source"), expires);
    }

    public IncomingIpBan expiringAt(Instant expires) {
        return new IncomingIpBan(ip, player, reason, source, requireNonNull(expires, "expires"));
    }

}
