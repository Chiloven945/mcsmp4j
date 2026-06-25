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
 * Capability summary inferred from {@code rpc.discover}.
 */
public final class McsmpCapabilities {

    private final Optional<McsmpProtocolVersion> protocolVersion;
    private final Set<DiscoveredMethod> methods;
    private final Set<DiscoveredNotification> notifications;
    private final Set<String> methodNames;
    private final Set<String> notificationNames;
    private final Set<McsmpFeature> features;
    private final JsonNode rawSchema;

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

    public Optional<McsmpProtocolVersion> protocolVersion() {
        return protocolVersion;
    }

    public Set<DiscoveredMethod> methods() {
        return methods;
    }

    public Set<DiscoveredNotification> notifications() {
        return notifications;
    }

    public Set<String> methodNames() {
        return methodNames;
    }

    public Set<String> notificationNames() {
        return notificationNames;
    }

    public Set<McsmpFeature> features() {
        return features;
    }

    public JsonNode rawSchema() {
        return rawSchema;
    }

    public boolean supportsMethod(String method) {
        return methodNames.contains(requireNonNull(method, "method"));
    }

    public boolean supportsNotification(String method) {
        return notificationNames.contains(requireNonNull(method, "method"));
    }

    public void require(McsmpFeature feature) {
        if (!supports(feature)) {
            throw new McsmpUnsupportedFeatureException(feature);
        }
    }

    public boolean supports(McsmpFeature feature) {
        return features.contains(requireNonNull(feature, "feature"));
    }

}
