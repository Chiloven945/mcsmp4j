package top.chiloven.mcsmp4j.event;

public record WorldUpgradeFinishedEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/world/upgrade_finished";

    @Override
    public String method() {
        return METHOD;
    }

}
