package top.chiloven.mcsmp4j.event;

public record WorldUpgradeProgressEvent(
        double progress
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/world/upgrade_progress";

    public WorldUpgradeProgressEvent {
        if (progress < 0.0d || progress > 1.0d) {
            throw new IllegalArgumentException("progress must be between 0 and 1");
        }
    }

    @Override
    public String method() {
        return METHOD;
    }

}
