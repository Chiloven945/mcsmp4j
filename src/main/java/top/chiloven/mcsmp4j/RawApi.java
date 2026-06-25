package top.chiloven.mcsmp4j;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

/**
 * Low-level JSON-RPC caller for official and custom MCSMP namespaces.
 */
public interface RawApi {

    CompletableFuture<JsonNode> callJson(
            String method,
            Object... params
    );

    <T> CompletableFuture<T> call(
            String method,
            Class<T> resultType,
            Object... params
    );

    <T> CompletableFuture<T> call(
            String method,
            TypeReference<T> resultType,
            Object... params
    );

}
