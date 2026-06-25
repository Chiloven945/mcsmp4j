package top.chiloven.mcsmp4j.event;

import static java.util.Objects.requireNonNull;

public record IpBanRemovedEvent(
        String ip
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/ip_bans/removed";

    public IpBanRemovedEvent {
        requireNonNull(ip, "ip");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
