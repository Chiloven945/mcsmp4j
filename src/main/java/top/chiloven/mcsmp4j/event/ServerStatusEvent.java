package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.ServerState;

import static java.util.Objects.requireNonNull;

public record ServerStatusEvent(
        ServerState status
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/server/status";

    public ServerStatusEvent {
        requireNonNull(status, "status");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
