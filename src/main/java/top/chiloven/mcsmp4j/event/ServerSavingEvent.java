package top.chiloven.mcsmp4j.event;

public record ServerSavingEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/saving";

    @Override
    public String method() {
        return METHOD;
    }

}
