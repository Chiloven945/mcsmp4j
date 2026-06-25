package top.chiloven.mcsmp4j.event;

import top.chiloven.mcsmp4j.model.Operator;

import static java.util.Objects.requireNonNull;

public record OperatorAddedEvent(
        Operator operator
) implements McsmpEvent {

    public static final String METHOD = "minecraft:notification/operators/added";

    public OperatorAddedEvent {
        requireNonNull(operator, "operator");
    }

    @Override
    public String method() {
        return METHOD;
    }

}
