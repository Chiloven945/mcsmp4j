package top.chiloven.mcsmp4j.event;

public record ServerSavedEvent() implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/saved";

    @Override
    public String method() {
        return METHOD;
    }

}
