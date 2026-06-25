package top.chiloven.mcsmp4j.internal;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import top.chiloven.mcsmp4j.McsmpProtocolException;
import top.chiloven.mcsmp4j.RawApi;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class RawApiImpl implements RawApi {

    private final JsonRpcTransport transport;
    private final ObjectMapper mapper;

    public RawApiImpl(
            JsonRpcTransport transport,
            ObjectMapper mapper
    ) {
        this.transport = requireNonNull(transport, "transport");
        this.mapper = requireNonNull(mapper, "mapper");
    }

    @Override
    public CompletableFuture<JsonNode> callJson(
            String method,
            Object... params
    ) {
        return transport.call(method, params);
    }

    @Override
    public <T> CompletableFuture<T> call(
            String method,
            Class<T> resultType,
            Object... params
    ) {
        requireNonNull(resultType, "resultType");
        return callJson(method, params).thenApply(node -> convert(node, resultType));
    }

    @Override
    public <T> CompletableFuture<T> call(
            String method,
            TypeReference<T> resultType,
            Object... params
    ) {
        requireNonNull(resultType, "resultType");
        return callJson(method, params).thenApply(node -> convert(node, resultType));
    }

    private <T> T convert(JsonNode node, Class<T> type) {
        if (type == Void.class || type == Void.TYPE) {
            return null;
        }
        try {
            return mapper.convertValue(node, type);
        } catch (IllegalArgumentException e) {
            throw new McsmpProtocolException("Could not decode JSON-RPC result as " + type.getName(), e);
        }
    }

    private <T> T convert(JsonNode node, TypeReference<T> type) {
        try {
            return mapper.convertValue(node, type);
        } catch (IllegalArgumentException e) {
            throw new McsmpProtocolException("Could not decode JSON-RPC result as " + type.getType(), e);
        }
    }

}
