package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.TypedGameRule;

import static java.util.Objects.requireNonNull;

public record GameRuleUpdatedEvent(
        TypedGameRule gamerule
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/gamerules/updated";

    public GameRuleUpdatedEvent {
        requireNonNull(gamerule, "gamerule");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
