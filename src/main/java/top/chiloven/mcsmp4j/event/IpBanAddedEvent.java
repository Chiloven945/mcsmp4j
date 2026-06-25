package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.IpBan;

import static java.util.Objects.requireNonNull;

public record IpBanAddedEvent(
        IpBan ban
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/ip_bans/added";

    public IpBanAddedEvent {
        requireNonNull(ban, "ban");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
