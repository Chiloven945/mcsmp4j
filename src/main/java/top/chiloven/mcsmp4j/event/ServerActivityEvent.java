package top.chiloven.mcsmp4j.event;

public record ServerActivityEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/activity";

    @Override
    public String method() {
        return METHOD;
    }

}
