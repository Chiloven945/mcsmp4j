package top.chiloven.mcsmp4j.version;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Semantic version value for the MCSMP protocol surface.
 *
 * <p>The protocol has a version history independent from this Java library's artifact version. A Minecraft server may
 * advertise a protocol version through discovery or expose enough methods and notifications for mcsmp4j to infer a
 * likely version. This record models the conventional {@code major.minor.patch} shape and provides comparison helpers
 * for feature checks and diagnostics.</p>
 *
 * @param major the major protocol version, increased for breaking changes
 * @param minor the minor protocol version, increased for compatible additions
 * @param patch the patch protocol version, increased for bug-fix-level changes
 */
public record McsmpProtocolVersion(
        int major,
        int minor,
        int patch
) implements Comparable<McsmpProtocolVersion> {

    /**
     * Initial protocol version known to mcsmp4j.
     */
    public static final McsmpProtocolVersion V1_0_0 = new McsmpProtocolVersion(1, 0, 0);

    /**
     * Protocol version that added the server activity notification.
     */
    public static final McsmpProtocolVersion V1_1_0 = new McsmpProtocolVersion(1, 1, 0);

    /**
     * Protocol version that introduced typed game-rule values.
     */
    public static final McsmpProtocolVersion V2_0_0 = new McsmpProtocolVersion(2, 0, 0);

    /**
     * Protocol version that made management discovery available before full server startup.
     */
    public static final McsmpProtocolVersion V3_0_0 = new McsmpProtocolVersion(3, 0, 0);

    /**
     * Protocol version that added world-upgrade notifications.
     */
    public static final McsmpProtocolVersion V3_1_0 = new McsmpProtocolVersion(3, 1, 0);

    private static final Pattern SEMVER = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:[-+].*)?$");

    /**
     * Validates that all semantic-version components are non-negative.
     */
    public McsmpProtocolVersion {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("protocol version components must not be negative");
        }
    }

    /**
     * Parses a semantic version string.
     *
     * <p>Build metadata and pre-release suffixes are accepted and ignored for ordering because MCSMP feature gates
     * currently use only the numeric components.</p>
     *
     * @param value the version string to parse
     *
     * @return the parsed version, or empty when the string does not contain a semantic version
     */
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

    /**
     * Checks whether this version is greater than or equal to another version.
     *
     * @param other the minimum required version
     *
     * @return {@code true} if this version is at least {@code other}
     */
    public boolean isAtLeast(McsmpProtocolVersion other) {
        return compareTo(other) >= 0;
    }

    /**
     * Compares two protocol versions by major, then minor, then patch.
     *
     * @param other the version to compare with
     *
     * @return a negative number, zero, or a positive number according to natural version ordering
     */
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

    /**
     * Returns the canonical {@code major.minor.patch} string.
     *
     * @return the semantic version string
     */
    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

}
