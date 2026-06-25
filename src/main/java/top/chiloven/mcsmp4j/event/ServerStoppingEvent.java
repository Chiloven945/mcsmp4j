package top.chiloven.mcsmp4j.event;

public record ServerStoppingEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/stopping";

    @Override
    public String method() {
        return METHOD;
    }

}
