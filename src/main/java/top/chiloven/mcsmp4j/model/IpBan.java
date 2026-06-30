package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * IP-ban entry returned by the {@code minecraft:ip_bans} API.
 *
 * <p>An IP ban targets a network address string and may carry metadata describing why it exists, who created it, when
 * it
 * was created, and when it expires. This is the server's authoritative stored shape after adding, setting, or listing
 * IP bans. Use {@link IncomingIpBan} for new add requests when the protocol accepts the narrower incoming shape.</p>
 *
 * <p>IP addresses can be sensitive operational data. Avoid logging or exposing them to untrusted users unless your
 * server
 * policy permits it.</p>
 *
 * @param ip      the banned IP address string
 * @param reason  optional reason supplied by the operator or server
 * @param source  optional source/actor that created the ban
 * @param expires optional expiration instant; {@code null} means permanent or unspecified
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IpBan(
        String ip,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

    /**
     * Validates that the IP address is present and optional text fields are non-blank.
     */
    public IpBan {
        requireNonNull(ip, "ip");
        if (ip.isBlank()) {
            throw new IllegalArgumentException("ip must not be blank");
        }
        if (reason != null && reason.isBlank()) {
            throw new IllegalArgumentException("reason must not be blank");
        }
        if (source != null && source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
    }

    /**
     * Creates a permanent IP ban with no reason.
     *
     * @param ip the IP address to ban
     *
     * @return a permanent IP ban entry
     */
    public static IpBan permanent(String ip) {
        return new IpBan(ip, null, null, null);
    }

    /**
     * Creates a permanent IP ban with a reason.
     *
     * @param ip     the IP address to ban
     * @param reason the ban reason
     *
     * @return a permanent IP ban entry with a reason
     */
    public static IpBan permanent(String ip, String reason) {
        return new IpBan(ip, requireNonNull(reason, "reason"), null, null);
    }

    /**
     * Creates a temporary IP ban.
     *
     * @param ip      the IP address to ban
     * @param expires the expiration instant
     *
     * @return a temporary IP ban entry
     */
    public static IpBan temporary(String ip, Instant expires) {
        return new IpBan(ip, null, null, requireNonNull(expires, "expires"));
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
