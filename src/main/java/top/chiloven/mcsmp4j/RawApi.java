package top.chiloven.mcsmp4j;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

/**
 * Low-level JSON-RPC caller for official and custom MCSMP namespaces.
 *
 * <p>The typed API groups in {@code top.chiloven.mcsmp4j.api} cover the official Minecraft namespace. This
 * interface remains available for advanced users who need to call newly introduced protocol methods before the library
 * adds typed wrappers, or methods exposed by modded servers under custom namespaces.</p>
 *
 * <p>Each call sends a JSON-RPC request over the active WebSocket connection. Method names must already be in
 * their full protocol form, for example {@code minecraft:server/status} or {@code my_mod:admin/reload}. Parameters are
 * encoded as the JSON-RPC {@code params} array in the order provided.</p>
 */
public interface RawApi {

    /**
     * Calls a JSON-RPC method and returns the raw JSON result node.
     *
     * <p>This is the most flexible method and is appropriate when the response shape is not known at compile
     * time. If the server returns a JSON-RPC error, the future completes exceptionally with
     * {@link McsmpRemoteException}. If the response cannot be decoded, it completes with
     * {@link McsmpProtocolException}.</p>
     *
     * @param method the full JSON-RPC method name to invoke
     * @param params zero or more positional JSON-RPC parameters
     *
     * @return a future that completes with the raw {@code result} JSON node
     */
    CompletableFuture<JsonNode> callJson(
            String method,
            Object... params
    );

    /**
     * Calls a JSON-RPC method and maps the result to a concrete Java class.
     *
     * <p>Use this overload for non-generic result types such as {@code ServerState.class},
     * {@code Boolean.class}, or a custom record class. For generic result types such as {@code List<Player>}, use
     * {@link #call(String, TypeReference, Object...)}.</p>
     *
     * @param method     the full JSON-RPC method name to invoke
     * @param resultType the class used to deserialize the result
     * @param params     zero or more positional JSON-RPC parameters
     * @param <T>        the expected Java result type
     *
     * @return a future that completes with the mapped result
     */
    <T> CompletableFuture<T> call(
            String method,
            Class<T> resultType,
            Object... params
    );

    /**
     * Calls a JSON-RPC method and maps the result to a Java type that may include generics.
     *
     * <p>Example usage:</p>
     *
     * <pre>{@code
     * CompletableFuture<List<Player>> players = client.raw().call(
     *         "minecraft:players",
     *         new TypeReference<List<Player>>() {}
     * );
     * }</pre>
     *
     * @param method     the full JSON-RPC method name to invoke
     * @param resultType the Jackson type reference used to deserialize the result
     * @param params     zero or more positional JSON-RPC parameters
     * @param <T>        the expected Java result type
     *
     * @return a future that completes with the mapped result
     */
    <T> CompletableFuture<T> call(
            String method,
            TypeReference<T> resultType,
            Object... params
    );

}
