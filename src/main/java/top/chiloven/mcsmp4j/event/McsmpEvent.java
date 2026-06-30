package top.chiloven.mcsmp4j.event;

/**
 * Marker interface for all typed MCSMP notification events.
 *
 * <p>Every event corresponds to one JSON-RPC notification method sent by the server. Implementations are Java records
 * for
 * notifications with payloads or empty records for marker notifications. The {@link #method()} value is always the
 * modern canonical notification method name, even when local compatibility mode accepted an older legacy prefix.</p>
 *
 * <p>Use this type as the upper bound when registering generic listeners with
 * {@link McsmpEvents#on(Class, java.util.function.Consumer)}.
 * For raw extension notifications that do not have a typed class, use {@link RawMcsmpEvent} or the protocol-level raw
 * notification listener.</p>
 */
public sealed interface McsmpEvent permits
        ServerStartedEvent,
        ServerStoppingEvent,
        ServerSavingEvent,
        ServerSavedEvent,
        ServerStatusEvent,
        ServerActivityEvent,
        PlayerJoinedEvent,
        PlayerLeftEvent,
        OperatorAddedEvent,
        OperatorRemovedEvent,
        AllowlistAddedEvent,
        AllowlistRemovedEvent,
        IpBanAddedEvent,
        IpBanRemovedEvent,
        UserBanAddedEvent,
        UserBanRemovedEvent,
        GameRuleUpdatedEvent,
        WorldUpgradeStartedEvent,
        WorldUpgradeProgressEvent,
        WorldUpgradeFinishedEvent,
        WorldUpgradeFailedEvent,
        RawMcsmpEvent {

    /**
     * Returns the JSON-RPC method name of the notification that produced this event.
     *
     * @return the full notification method name
     */
    String method();

}
