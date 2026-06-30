package top.chiloven.mcsmp4j.discovery;

import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.McsmpUnsupportedFeatureException;
import top.chiloven.mcsmp4j.version.McsmpFeature;
import top.chiloven.mcsmp4j.version.McsmpProtocolVersion;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;

/**
 * Immutable snapshot of methods, notifications, protocol version, and named features advertised by a server.
 *
 * <p>{@code McsmpCapabilities} is produced by {@link McsmpDiscovery#discover()} and cached by
 * {@link top.chiloven.mcsmp4j.McsmpClient#discover()}. It answers two kinds of questions:</p>
 *
 * <ul>
 *     <li>Does the server advertise a specific JSON-RPC method or notification name?</li>
 *     <li>Does the advertised surface imply a higher-level protocol-history feature such as typed game-rule values?</li>
 * </ul>
 *
 * <p>Prefer feature checks for application decisions and raw method/notification checks for diagnostics or extension
 * support. A custom server may expose methods in combinations that do not match any historical vanilla release, so a
 * feature should be understood as "this behavior appears supported" rather than as a guarantee of every detail of a
 * Minecraft release.</p>
 */
public final class McsmpCapabilities {

    private final Optional<McsmpProtocolVersion> protocolVersion;
    private final Set<DiscoveredMethod> methods;
    private final Set<DiscoveredNotification> notifications;
    private final Set<String> methodNames;
    private final Set<String> notificationNames;
    private final Set<McsmpFeature> features;
    private final JsonNode rawSchema;

    /**
     * Creates an immutable capability summary.
     *
     * @param protocolVersion the optional advertised protocol version
     * @param methods         discovered JSON-RPC methods
     * @param notifications   discovered JSON-RPC notifications
     * @param features        inferred feature flags
     * @param rawSchema       the complete discovery schema returned by the server
     */
    public McsmpCapabilities(
            Optional<McsmpProtocolVersion> protocolVersion,
            Collection<DiscoveredMethod> methods,
            Collection<DiscoveredNotification> notifications,
            Collection<McsmpFeature> features,
            JsonNode rawSchema
    ) {
        this.protocolVersion = requireNonNull(protocolVersion, "protocolVersion");
        this.methods = Set.copyOf(requireNonNull(methods, "methods"));
        this.notifications = Set.copyOf(requireNonNull(notifications, "notifications"));
        this.methodNames = names(this.methods);
        this.notificationNames = names(this.notifications);
        this.features = Set.copyOf(requireNonNull(features, "features"));
        this.rawSchema = requireNonNull(rawSchema, "rawSchema");
    }

    private static Set<String> names(Collection<? extends Record> records) {
        var result = new TreeSet<String>();
        for (var record : records) {
            if (record instanceof DiscoveredMethod method) {
                result.add(method.name());
            } else if (record instanceof DiscoveredNotification notification) {
                result.add(notification.name());
            }
        }
        return Set.copyOf(result);
    }

    /**
     * Returns the advertised MCSMP protocol version if discovery exposed one.
     *
     * @return the protocol version, or empty when the schema did not include a parsable version
     */
    public Optional<McsmpProtocolVersion> protocolVersion() {
        return protocolVersion;
    }

    /**
     * Returns the discovered method entries.
     *
     * @return an immutable set of discovered methods
     */
    public Set<DiscoveredMethod> methods() {
        return methods;
    }

    /**
     * Returns the discovered notification entries.
     *
     * @return an immutable set of discovered notifications
     */
    public Set<DiscoveredNotification> notifications() {
        return notifications;
    }

    /**
     * Returns only the discovered method names.
     *
     * @return an immutable set of method names
     */
    public Set<String> methodNames() {
        return methodNames;
    }

    /**
     * Returns only the discovered notification names.
     *
     * @return an immutable set of notification names
     */
    public Set<String> notificationNames() {
        return notificationNames;
    }

    /**
     * Returns feature flags inferred from protocol version and discovered names.
     *
     * @return an immutable set of supported features
     */
    public Set<McsmpFeature> features() {
        return features;
    }

    /**
     * Returns the complete raw discovery schema.
     *
     * <p>This is useful for diagnostics and for advanced clients that need schema details not exposed by the
     * high-level capability model.</p>
     *
     * @return the raw schema JSON node returned by {@code rpc.discover}
     */
    public JsonNode rawSchema() {
        return rawSchema;
    }

    /**
     * Checks whether a JSON-RPC method was advertised.
     *
     * @param method the full method name to check
     *
     * @return {@code true} if the method was discovered
     */
    public boolean supportsMethod(String method) {
        return methodNames.contains(requireNonNull(method, "method"));
    }

    /**
     * Checks whether a JSON-RPC notification was advertised.
     *
     * @param method the full notification method name to check
     *
     * @return {@code true} if the notification was discovered
     */
    public boolean supportsNotification(String method) {
        return notificationNames.contains(requireNonNull(method, "method"));
    }

    /**
     * Requires a feature to be present, throwing a descriptive exception otherwise.
     *
     * @param feature the feature required by the caller
     *
     * @throws McsmpUnsupportedFeatureException if the feature is not in {@link #features()}
     */
    public void require(McsmpFeature feature) {
        if (!supports(feature)) {
            throw new McsmpUnsupportedFeatureException(feature);
        }
    }

    /**
     * Checks whether a feature is supported.
     *
     * @param feature the feature to check
     *
     * @return {@code true} if the feature is present in this capability snapshot
     */
    public boolean supports(McsmpFeature feature) {
        return features.contains(requireNonNull(feature, "feature"));
    }

}
