package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

public record AllowlistRemovedEvent(
        Player player
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/allowlist/removed";

    public AllowlistRemovedEvent {
        requireNonNull(player, "player");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
