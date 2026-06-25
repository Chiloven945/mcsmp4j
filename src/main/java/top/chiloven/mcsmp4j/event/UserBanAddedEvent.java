package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.UserBan;

import static java.util.Objects.requireNonNull;

public record UserBanAddedEvent(
        UserBan ban
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/bans/added";

    public UserBanAddedEvent {
        requireNonNull(ban, "ban");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
