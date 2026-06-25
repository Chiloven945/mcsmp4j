package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

public record WorldUpgradeFailedEvent(
        String reason
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/world/upgrade_failed";

    public WorldUpgradeFailedEvent {
        requireNonNull(reason, "reason");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
