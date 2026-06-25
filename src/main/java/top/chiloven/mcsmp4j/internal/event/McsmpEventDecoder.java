package top.chiloven.mcsmp4j.internal.event;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.NullNode;
import top.chiloven.mcsmp4j.McsmpProtocolException;
import top.chiloven.mcsmp4j.event.*;
import top.chiloven.mcsmp4j.model.*;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;

public final class McsmpEventDecoder {

    private static final String LEGACY_PREFIX = "notification:";
    private static final String MODERN_PREFIX = "minecraft:notification/";

    private final ObjectMapper mapper;
    private final boolean legacyNotificationPrefix;
    private final Map<String, Decoder> decoders;

    public McsmpEventDecoder(
            ObjectMapper mapper,
            boolean legacyNotificationPrefix
    ) {
        this.mapper = requireNonNull(mapper, "mapper");
        this.legacyNotificationPrefix = legacyNotificationPrefix;
        this.decoders = createDecoders();
    }

    private static Decoder noArgs(Supplier<? extends McsmpEvent> factory) {
        return ignored -> factory.get();
    }

    private static String stringValue(@Nullable JsonNode node) {
        if (node == null || node.isNull()) {
            throw new McsmpProtocolException("Expected string notification payload, got null");
        }
        if (!node.isString()) {
            throw new McsmpProtocolException("Expected string notification payload");
        }
        return node.asString();
    }

    private static double numberValue(@Nullable JsonNode node) {
        if (node == null || node.isNull()) {
            throw new McsmpProtocolException("Expected numeric notification payload, got null");
        }
        if (!node.isNumber()) {
            throw new McsmpProtocolException("Expected numeric notification payload");
        }
        return node.asDouble();
    }

    public Optional<McsmpEvent> decode(JsonRpcNotification notification) {
        requireNonNull(notification, "notification");

        var method = normalize(notification.method());
        if (method == null) return Optional.empty();

        var decoder = decoders.get(method);
        if (decoder == null) return Optional.empty();

        return Optional.ofNullable(decoder.decode(notification.params()));
    }

    private @Nullable String normalize(@Nullable String method) {
        if (method == null) return null;

        if (method.startsWith(MODERN_PREFIX)) {
            return method;
        }
        if (legacyNotificationPrefix && method.startsWith(LEGACY_PREFIX)) {
            return MODERN_PREFIX + method.substring(LEGACY_PREFIX.length());
        }

        return null;
    }

    private Map<String, Decoder> createDecoders() {
        return Map.ofEntries(
                entry(
                        ServerStartedEvent.METHOD,
                        noArgs(ServerStartedEvent::new)
                ),
                entry(
                        ServerStoppingEvent.METHOD,
                        noArgs(ServerStoppingEvent::new)
                ),
                entry(
                        ServerSavingEvent.METHOD,
                        noArgs(ServerSavingEvent::new)
                ),
                entry(
                        ServerSavedEvent.METHOD,
                        noArgs(ServerSavedEvent::new)
                ),
                entry(
                        ServerActivityEvent.METHOD,
                        noArgs(ServerActivityEvent::new)
                ),
                entry(
                        ServerStatusEvent.METHOD,
                        payload(ServerState.class, ServerStatusEvent::new, "status")
                ),
                entry(
                        PlayerJoinedEvent.METHOD,
                        payload(Player.class, PlayerJoinedEvent::new, "player")
                ),
                entry(
                        PlayerLeftEvent.METHOD,
                        payload(Player.class, PlayerLeftEvent::new, "player")
                ),
                entry(
                        OperatorAddedEvent.METHOD,
                        payload(Operator.class, OperatorAddedEvent::new, "player", "operator")
                ),
                entry(
                        OperatorRemovedEvent.METHOD,
                        payload(Operator.class, OperatorRemovedEvent::new, "player", "operator")
                ),
                entry(
                        AllowlistAddedEvent.METHOD,
                        payload(Player.class, AllowlistAddedEvent::new, "player")
                ),
                entry(
                        AllowlistRemovedEvent.METHOD,
                        payload(Player.class, AllowlistRemovedEvent::new, "player")
                ),
                entry(
                        IpBanAddedEvent.METHOD,
                        payload(IpBan.class, IpBanAddedEvent::new, "player", "ban", "ip_ban")
                ),
                entry(
                        IpBanRemovedEvent.METHOD,
                        params -> new IpBanRemovedEvent(stringValue(pick(params, "ip", "player")))
                ),
                entry(
                        UserBanAddedEvent.METHOD,
                        payload(UserBan.class, UserBanAddedEvent::new, "player", "ban")
                ),
                entry(
                        UserBanRemovedEvent.METHOD,
                        payload(Player.class, UserBanRemovedEvent::new, "player")
                ),
                entry(
                        GameRuleUpdatedEvent.METHOD,
                        payload(TypedGameRule.class, GameRuleUpdatedEvent::new, "gamerule")
                ),
                entry(
                        WorldUpgradeStartedEvent.METHOD,
                        noArgs(WorldUpgradeStartedEvent::new)
                ),
                entry(
                        WorldUpgradeProgressEvent.METHOD,
                        params -> new WorldUpgradeProgressEvent(numberValue(pick(params, "progress")))
                ),
                entry(
                        WorldUpgradeFinishedEvent.METHOD,
                        noArgs(WorldUpgradeFinishedEvent::new)
                ),
                entry(
                        WorldUpgradeFailedEvent.METHOD,
                        params -> new WorldUpgradeFailedEvent(stringValue(pick(params, "reason")))
                )
        );
    }

    private <T> Decoder payload(
            Class<T> type,
            PayloadFactory<T> factory,
            String... keys
    ) {
        return params -> factory.create(convert(pick(params, keys), type));
    }

    private <T> T convert(JsonNode node, Class<T> type) {
        try {
            return mapper.convertValue(node, type);
        } catch (IllegalArgumentException e) {
            throw new McsmpProtocolException("Could not decode MCSMP notification payload as " + type.getName(), e);
        }
    }

    private JsonNode pick(@Nullable JsonNode params, String... keys) {
        if (params == null || params.isNull()) {
            return NullNode.getInstance();
        }

        if (params.isArray()) {
            if (params.isEmpty()) {
                return NullNode.getInstance();
            }

            var first = params.get(0);
            if (first != null && first.isObject()) {
                var named = pickNamed(first, keys);
                if (named != null) {
                    return named;
                }
            }

            return first == null ? NullNode.getInstance() : first;
        }

        if (params.isObject()) {
            var named = pickNamed(params, keys);
            return named == null ? params : named;
        }

        return params;
    }

    private @Nullable JsonNode pickNamed(JsonNode object, String... keys) {
        return Arrays.stream(keys)
                .map(object::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @FunctionalInterface
    private interface Decoder {

        @Nullable McsmpEvent decode(@Nullable JsonNode params);

    }

    @FunctionalInterface
    private interface PayloadFactory<T> {

        McsmpEvent create(T payload);

    }

}
