package top.chiloven.mcsmp4j.event;

import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

public record RawMcsmpEvent(
        String method,
        JsonNode params
) implements McsmpEvent {

    public RawMcsmpEvent {
        requireNonNull(method, "method");
        requireNonNull(params, "params");
    }

}
