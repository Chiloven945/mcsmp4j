package top.chiloven.mcsmp4j.version;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Semantic version of the MCSMP protocol when advertised by {@code rpc.discover}.
 */
public record McsmpProtocolVersion(
        int major,
        int minor,
        int patch
) implements Comparable<McsmpProtocolVersion> {

    public static final McsmpProtocolVersion
            V1_0_0 = new McsmpProtocolVersion(1, 0, 0),
            V1_1_0 = new McsmpProtocolVersion(1, 1, 0),
            V2_0_0 = new McsmpProtocolVersion(2, 0, 0),
            V3_0_0 = new McsmpProtocolVersion(3, 0, 0),
            V3_1_0 = new McsmpProtocolVersion(3, 1, 0);

    private static final Pattern SEMVER = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:[-+].*)?$");

    public McsmpProtocolVersion {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("protocol version components must not be negative");
        }
    }

    public static Optional<McsmpProtocolVersion> parse(String value) {
        Objects.requireNonNull(value, "value");
        var matcher = SEMVER.matcher(value.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new McsmpProtocolVersion(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3))
        ));
    }

    public boolean isAtLeast(McsmpProtocolVersion other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(McsmpProtocolVersion other) {
        Objects.requireNonNull(other, "other");
        int byMajor = Integer.compare(major, other.major);
        if (byMajor != 0) {
            return byMajor;
        }
        int byMinor = Integer.compare(minor, other.minor);
        if (byMinor != 0) {
            return byMinor;
        }
        return Integer.compare(patch, other.patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

}
