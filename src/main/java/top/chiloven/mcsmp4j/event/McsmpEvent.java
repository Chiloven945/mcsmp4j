package top.chiloven.mcsmp4j.event;

/**
 * Base type for all high-level MCSMP notifications decoded by mcsmp4j.
 *
 * <p>Every official notification event record implements this sealed interface and exposes the original
 * JSON-RPC notification method through {@link #method()}. Unknown notifications and custom namespace notifications are
 * represented by {@link RawMcsmpEvent}.</p>
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
