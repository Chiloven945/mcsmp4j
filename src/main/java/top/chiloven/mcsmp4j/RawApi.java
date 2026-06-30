package top.chiloven.mcsmp4j;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

/**
 * Low-level JSON-RPC caller for advanced MCSMP usage.
 *
 * <p>{@code RawApi} is the escape hatch beneath the typed official APIs. It lets applications call any JSON-RPC method
 * by
 * name and choose the Java result type. This is essential for custom namespaces, modded servers, diagnostics, and newly
 * introduced protocol methods that are not yet represented by a dedicated mcsmp4j interface.</p>
 *
 * <h2>Parameter shape</h2>
 *
 * <p>The {@code params} varargs correspond to the JSON-RPC positional parameter array. Passing no parameters omits or
 * sends an empty parameter list depending on the transport implementation. Passing one Java collection creates one
 * positional parameter whose JSON value is an array; this matches official methods such as
 * {@code minecraft:allowlist/add}, which accept a single array parameter containing players.</p>
 *
 * <h2>Type conversion</h2>
 *
 * <p>{@link #call(String, Class, Object...)} is convenient for simple result types. Use
 * {@link #call(String, tools.jackson.core.type.TypeReference, Object...)} for parameterized results such as
 * {@code List<Player>} or map-like extension payloads. {@link #callJson(String, Object...)} returns a raw Jackson tree
 * and is the best choice while inspecting unknown or unstable extension methods.</p>
 *
 * <h2>Error semantics</h2>
 *
 * <p>The returned future completes exceptionally with {@link McsmpRemoteException} if the remote endpoint returns a
 * JSON-RPC error object, with {@link McsmpTimeoutException} if no matching response arrives before the configured
 * request timeout, and with a transport/protocol exception for malformed responses or closed connections.</p>
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
