package top.chiloven.mcsmp4j.internal.discovery;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.discovery.DiscoveredMethod;
import top.chiloven.mcsmp4j.discovery.DiscoveredNotification;
import top.chiloven.mcsmp4j.discovery.McsmpCapabilities;
import top.chiloven.mcsmp4j.version.McsmpFeature;
import top.chiloven.mcsmp4j.version.McsmpProtocolVersion;

import java.util.*;

import static java.util.Objects.requireNonNull;

final class McsmpCapabilitiesParser {

    private static final Set<String> VERSION_KEYS = Set.of(
            "protocolVersion",
            "protocol_version",
            "mcsmpProtocolVersion",
            "managementProtocolVersion",
            "management_protocol_version",
            "mcsmp_version",
            "mcsmpVersion"
    );

    private McsmpCapabilitiesParser() {
    }

    static McsmpCapabilities parse(JsonNode schema) {
        requireNonNull(schema, "schema");
        var methods = new TreeMap<String, DiscoveredMethod>();
        var notifications = new TreeMap<String, DiscoveredNotification>();

        collectMethodsSection(schema.get("methods"), methods);
        collectNotificationsSection(schema.get("notifications"), notifications);
        collectNotificationsSection(schema.get("events"), notifications);

        collectByKnownKeys(schema, methods, notifications);
        collectStringLeaves(schema, methods, notifications);

        // The endpoint itself exists if this parser is running successfully.
        methods.putIfAbsent("rpc.discover", new DiscoveredMethod("rpc.discover", null));

        var version = findProtocolVersion(schema);
        var features = inferFeatures(version, methods.keySet(), notifications.keySet());
        return new McsmpCapabilities(version, methods.values(), notifications.values(), features, schema);
    }

    private static void collectMethodsSection(
            @Nullable JsonNode node,
            Map<String, DiscoveredMethod> methods
    ) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            for (var entry : node.properties()) {
                var name = entry.getKey();
                if (isProtocolName(name)) {
                    if (!isNotificationName(name)) {
                        methods.putIfAbsent(name, new DiscoveredMethod(name, entry.getValue()));
                    }
                }
                collectMethodsSection(entry.getValue(), methods);
            }
            return;
        }
        if (node.isArray()) {
            for (var item : node) {
                collectMethodsSection(item, methods);
            }
            return;
        }
        if (node.isString()
                && isProtocolName(node.asString())
                && !isNotificationName(node.asString())) {
            var name = node.asString();
            methods.putIfAbsent(name, new DiscoveredMethod(name, null));
        }
    }

    private static void collectNotificationsSection(
            @Nullable JsonNode node,
            Map<String, DiscoveredNotification> notifications
    ) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isObject()) {
            var directName = firstString(node, "name", "method", "notification", "path");
            if (directName != null && isProtocolName(directName)) {
                notifications.putIfAbsent(directName, new DiscoveredNotification(directName, node));
            }
            for (var entry : node.properties()) {
                var name = entry.getKey();
                if (isProtocolName(name)) {
                    notifications.putIfAbsent(name, new DiscoveredNotification(name, entry.getValue()));
                }
                collectNotificationsSection(entry.getValue(), notifications);
            }
            return;
        }
        if (node.isArray()) {
            for (var item : node) {
                collectNotificationsSection(item, notifications);
            }
            return;
        }
        if (node.isString() && isProtocolName(node.asString())) {
            var name = node.asString();
            notifications.putIfAbsent(name, new DiscoveredNotification(name, null));
        }
    }

    private static void collectByKnownKeys(
            JsonNode node,
            Map<String, DiscoveredMethod> methods,
            Map<String, DiscoveredNotification> notifications
    ) {
        if (node.isObject()) {
            var method = firstString(node, "method", "rpcMethod");
            if (method != null && isProtocolName(method) && !isNotificationName(method)) {
                methods.putIfAbsent(method, new DiscoveredMethod(method, node));
            }
            var notification = firstString(node, "notification", "event");
            if (notification != null && isProtocolName(notification)) {
                notifications.putIfAbsent(notification, new DiscoveredNotification(notification, node));
            }
            var name = firstString(node, "name");
            if (name != null && isProtocolName(name)) {
                if (isNotificationName(name)) {
                    notifications.putIfAbsent(name, new DiscoveredNotification(name, node));
                } else {
                    methods.putIfAbsent(name, new DiscoveredMethod(name, node));
                }
            }
            for (var entry : node.properties()) {
                collectByKnownKeys(entry.getValue(), methods, notifications);
            }
        } else if (node.isArray()) {
            for (var item : node) {
                collectByKnownKeys(item, methods, notifications);
            }
        }
    }

    private static void collectStringLeaves(
            JsonNode node,
            Map<String, DiscoveredMethod> methods,
            Map<String, DiscoveredNotification> notifications
    ) {
        if (node.isString()) {
            var value = node.asString();
            if (isProtocolName(value)) {
                if (isNotificationName(value)) {
                    notifications.putIfAbsent(value, new DiscoveredNotification(value, null));
                } else {
                    methods.putIfAbsent(value, new DiscoveredMethod(value, null));
                }
            }
            return;
        }
        if (node.isObject()) {
            for (var entry : node.properties()) {
                var key = entry.getKey();
                if (isProtocolName(key)) {
                    if (isNotificationName(key)) {
                        notifications.putIfAbsent(key, new DiscoveredNotification(key, entry.getValue()));
                    } else {
                        methods.putIfAbsent(key, new DiscoveredMethod(key, entry.getValue()));
                    }
                }
                collectStringLeaves(entry.getValue(), methods, notifications);
            }
        } else if (node.isArray()) {
            for (var item : node) {
                collectStringLeaves(item, methods, notifications);
            }
        }
    }

    private static Optional<McsmpProtocolVersion> findProtocolVersion(JsonNode schema) {
        var topLevel = versionFromKnownKeys(schema);
        if (topLevel.isPresent()) {
            return topLevel;
        }
        return findProtocolVersionRecursive(schema);
    }

    private static Optional<McsmpProtocolVersion> findProtocolVersionRecursive(JsonNode node) {
        if (!node.isObject()) {
            if (node.isArray()) {
                for (var item : node) {
                    var found = findProtocolVersionRecursive(item);
                    if (found.isPresent()) {
                        return found;
                    }
                }
            }
            return Optional.empty();
        }
        var direct = versionFromKnownKeys(node);
        if (direct.isPresent()) {
            return direct;
        }
        for (var entry : node.properties()) {
            var found = findProtocolVersionRecursive(entry.getValue());
            if (found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }

    private static Optional<McsmpProtocolVersion> versionFromKnownKeys(JsonNode node) {
        if (!node.isObject()) {
            return Optional.empty();
        }
        for (var key : VERSION_KEYS) {
            var value = node.get(key);
            if (value == null || value.isNull()) {
                continue;
            }
            if (value.isString()) {
                var parsed = McsmpProtocolVersion.parse(value.asString());
                if (parsed.isPresent()) {
                    return parsed;
                }
            } else if (value.isObject()) {
                var parsed = versionFromObject(value);
                if (parsed.isPresent()) {
                    return parsed;
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<McsmpProtocolVersion> versionFromObject(JsonNode node) {
        var major = node.get("major");
        var minor = node.get("minor");
        var patch = node.get("patch");
        if (major != null && minor != null && patch != null
                && major.isIntegralNumber() && minor.isIntegralNumber() && patch.isIntegralNumber()) {
            return Optional.of(new McsmpProtocolVersion(major.asInt(), minor.asInt(), patch.asInt()));
        }
        return Optional.empty();
    }

    private static Set<McsmpFeature> inferFeatures(
            Optional<McsmpProtocolVersion> version,
            Set<String> methods,
            Set<String> notifications
    ) {
        var features = EnumSet.noneOf(McsmpFeature.class);
        version.ifPresent(value -> {
            if (value.isAtLeast(McsmpProtocolVersion.V1_0_0)) {
                features.add(McsmpFeature.AUTHENTICATION);
                features.add(McsmpFeature.TLS_BY_DEFAULT);
                features.add(McsmpFeature.MINECRAFT_NOTIFICATION_PREFIX);
            }
            if (value.isAtLeast(McsmpProtocolVersion.V1_1_0)) {
                features.add(McsmpFeature.SERVER_ACTIVITY_NOTIFICATION);
                features.add(McsmpFeature.ORIGIN_ALLOWLIST);
            }
            if (value.isAtLeast(McsmpProtocolVersion.V2_0_0)) {
                features.add(McsmpFeature.TYPED_GAMERULE_VALUE);
            }
            if (value.isAtLeast(McsmpProtocolVersion.V3_0_0)) {
                features.add(McsmpFeature.PRE_START_DISCOVERY);
            }
            if (value.isAtLeast(McsmpProtocolVersion.V3_1_0)) {
                features.add(McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS);
            }
        });

        if (notifications.stream().anyMatch(name -> name.startsWith("minecraft:notification/"))) {
            features.add(McsmpFeature.MINECRAFT_NOTIFICATION_PREFIX);
        }
        if (notifications.contains("minecraft:notification/server/activity")) {
            features.add(McsmpFeature.SERVER_ACTIVITY_NOTIFICATION);
        }
        if (methods.contains("rpc.discover")) {
            features.add(McsmpFeature.PRE_START_DISCOVERY);
        }
        if (notifications.stream().anyMatch(name -> name.startsWith("minecraft:notification/world/upgrade_"))) {
            features.add(McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS);
        }
        if (methods.contains("minecraft:gamerules/update")) {
            // The endpoint exists before 2.0.0 too, but this is still useful for servers that omit the version
            // while returning typed values in their discovery schema. Keep old string decoding in the model regardless.
            var maybeTyped = notifications.contains("minecraft:notification/gamerules/updated");
            if (maybeTyped && version.isPresent() && version.get().isAtLeast(McsmpProtocolVersion.V2_0_0)) {
                features.add(McsmpFeature.TYPED_GAMERULE_VALUE);
            }
        }
        return Set.copyOf(features);
    }

    private static @Nullable String firstString(JsonNode node, String... keys) {
        return Arrays.stream(keys)
                .map(node::get)
                .filter(value -> value != null && value.isString())
                .findFirst()
                .map(JsonNode::asString)
                .orElse(null);
    }

    private static boolean isProtocolName(String value) {
        return value.startsWith("minecraft:") || value.startsWith("rpc.") || value.startsWith("notification:");
    }

    private static boolean isNotificationName(String value) {
        return value.startsWith("minecraft:notification/") || value.startsWith("notification:");
    }

}
