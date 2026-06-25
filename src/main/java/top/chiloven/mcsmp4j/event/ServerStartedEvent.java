package top.chiloven.mcsmp4j.event;

public record ServerStartedEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/started";

    @Override
    public String method() {
        return METHOD;
    }

}
