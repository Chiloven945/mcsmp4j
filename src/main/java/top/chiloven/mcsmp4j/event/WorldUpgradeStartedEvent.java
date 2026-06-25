package top.chiloven.mcsmp4j.event;

public record WorldUpgradeStartedEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/world/upgrade_started";

    @Override
    public String method() {
        return METHOD;
    }

}
