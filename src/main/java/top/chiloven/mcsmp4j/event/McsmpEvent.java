package top.chiloven.mcsmp4j.event;

/**
 * Base type for all high-level MCSMP notifications decoded by mcsmp4j.
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

    String method();

}
