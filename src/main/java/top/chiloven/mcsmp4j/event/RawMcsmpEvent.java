package top.chiloven.mcsmp4j.event;

import tools.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

/**
 * Raw event wrapper exposed by the typed event registry.
 *
 * <p>This record keeps the canonical or extension notification method name and its raw parameter tree. It is useful
 * for
 * logging, debugging, custom namespaces, and protocol versions newer than this library release. If a typed class exists
 * for an event, prefer the typed class for application logic and use raw events for diagnostics.</p>
 *
 * @param method the full notification method name
 * @param params raw notification parameters as received after any configured legacy-prefix normalization
 */
public record RawMcsmpEvent(
        String method,
        JsonNode params
) implements McsmpEvent {

    /**
     * Validates that both method and parameter JSON are present.
     */
    public RawMcsmpEvent {
        requireNonNull(method, "method");
        requireNonNull(params, "params");
    }

}
