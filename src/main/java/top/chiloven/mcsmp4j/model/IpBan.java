package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * IP ban entry returned by the MCSMP IP ban list endpoints.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IpBan(
        String ip,
        @Nullable String reason,
        @Nullable String source,
        @Nullable Instant expires
) {

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

    public static IpBan permanent(String ip) {
        return new IpBan(ip, null, null, null);
    }

    public static IpBan permanent(String ip, String reason) {
        return new IpBan(ip, requireNonNull(reason, "reason"), null, null);
    }

    public static IpBan temporary(String ip, Instant expires) {
        return new IpBan(ip, null, null, requireNonNull(expires, "expires"));
    }

    public boolean hasExpiration() {
        return expires != null;
    }

}
