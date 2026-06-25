package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

public record AllowlistAddedEvent(
        Player player
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/allowlist/added";

    public AllowlistAddedEvent {
        requireNonNull(player, "player");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
