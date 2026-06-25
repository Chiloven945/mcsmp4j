package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

public record PlayerLeftEvent(
        Player player
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/players/left";

    public PlayerLeftEvent {
        requireNonNull(player, "player");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
