package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Player;

import static java.util.Objects.requireNonNull;

public record PlayerJoinedEvent(
        Player player
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/players/joined";

    public PlayerJoinedEvent {
        requireNonNull(player, "player");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
