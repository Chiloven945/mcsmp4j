package top.chiloven.mcsmp4j.internal;

import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.NullNode;
import top.chiloven.mcsmp4j.McsmpProtocolException;
import top.chiloven.mcsmp4j.protocol.JsonRpcError;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;

import static java.util.Objects.requireNonNull;

final class JsonRpcCodec {

    static final String JSONRPC_VERSION = "2.0";

    private final ObjectMapper mapper;

    JsonRpcCodec(
            ObjectMapper mapper
    ) {
        this.mapper = requireNonNull(mapper, "mapper");
    }

    String encodeRequest(long id, String method, Object[] params) {
        requireNonNull(method, "method");
        requireNonNull(params, "params");
        if (method.isBlank()) {
            throw new IllegalArgumentException("method must not be blank");
        }

        var request = mapper.createObjectNode();
        request.put("jsonrpc", JSONRPC_VERSION);
        request.put("method", method);
        request.put("id", id);
        if (params.length > 0) {
            var array = mapper.createArrayNode();
            for (var param : params) {
                var node = mapper.valueToTree(param);
                array.add(node == null ? NullNode.getInstance() : node);
            }
            request.set("params", array);
        }

        try {
            return mapper.writeValueAsString(request);
        } catch (JacksonException e) {
            throw new McsmpProtocolException("Could not encode JSON-RPC request", e);
        }
    }

    ParsedMessage decode(String payload) {
        JsonNode root;
        try {
            root = mapper.readTree(payload);
        } catch (JacksonException e) {
            throw new McsmpProtocolException("Received invalid JSON from MCSMP server", e);
        }

        if (root == null || !root.isObject()) {
            throw new McsmpProtocolException("JSON-RPC message must be an object");
        }

        var id = root.get("id");
        if (id != null && (root.has("result") || root.has("error"))) {
            return ParsedMessage.response(decodeResponse(root, id));
        }

        var method = root.get("method");
        if (method != null && method.isString() && id == null) {
            var params = root.get("params");
            return ParsedMessage.notification(
                    new JsonRpcNotification(method.asString(), params == null
                            ? NullNode.getInstance()
                            : params)
            );
        }

        throw new McsmpProtocolException("Unsupported JSON-RPC message: expected response or notification");
    }

    private JsonRpcResponseMessage decodeResponse(JsonNode root, JsonNode id) {
        var idKey = idToKey(id);
        var errorNode = root.get("error");
        if (errorNode != null && !errorNode.isNull()) {
            if (!errorNode.isObject()) {
                throw new McsmpProtocolException("JSON-RPC error must be an object");
            }
            int code = required(errorNode, "code").asInt();
            var message = required(errorNode, "message").asString();
            var data = errorNode.get("data");
            return JsonRpcResponseMessage.error(idKey, new JsonRpcError(code, message, data));
        }

        var result = root.get("result");
        if (result == null) {
            result = NullNode.getInstance();
        }

        var embeddedError = embeddedError(result);
        if (embeddedError != null) {
            return JsonRpcResponseMessage.error(idKey, embeddedError);
        }

        return JsonRpcResponseMessage.result(idKey, result);
    }

    static String idToKey(JsonNode id) {
        if (id.isIntegralNumber() || id.isBoolean()) {
            return id.toString();
        }
        if (id.isString()) {
            return id.asString();
        }
        if (id.isNull()) {
            return "null";
        }
        return id.toString();
    }

    private static JsonNode required(JsonNode node, String property) {
        var value = node.get(property);
        if (value == null || value.isNull()) {
            throw new McsmpProtocolException("JSON-RPC error missing required property: " + property);
        }
        return value;
    }

    /**
     * Some development snapshots and proxy layers have been observed to wrap a JSON-RPC-like error inside
     * {@code result}. Treat that shape as an error so callers do not accidentally deserialize a failure payload as an
     * application model.
     */
    private static @Nullable JsonRpcError embeddedError(JsonNode result) {
        if (!result.isObject()) {
            return null;
        }

        var errorNode = result.get("error");
        if (errorNode != null && errorNode.isObject()) {
            var code = errorNode.get("code");
            var message = errorNode.get("message");
            if (code != null && code.isIntegralNumber() && message != null && message.isString()) {
                return new JsonRpcError(code.asInt(), message.asString(), errorNode.get("data"));
            }
        }

        var code = result.get("code");
        var message = result.get("message");
        if (code != null && code.isIntegralNumber()
                && message != null && message.isString()
                && (result.has("error") || result.has("data"))) {
            return new JsonRpcError(code.asInt(), message.asString(), result.get("data"));
        }

        return null;
    }

    sealed interface ParsedMessage permits ParsedMessage.Response, ParsedMessage.Notification {

        static ParsedMessage response(JsonRpcResponseMessage response) {
            return new Response(response);
        }

        static ParsedMessage notification(JsonRpcNotification notification) {
            return new Notification(notification);
        }

        record Response(JsonRpcResponseMessage response) implements ParsedMessage {

        }

        record Notification(JsonRpcNotification notification) implements ParsedMessage {

        }

    }

    record JsonRpcResponseMessage(
            String idKey,
            @Nullable JsonNode result,
            @Nullable JsonRpcError error
    ) {

        static JsonRpcResponseMessage result(String idKey, JsonNode result) {
            return new JsonRpcResponseMessage(idKey, result, null);
        }

        static JsonRpcResponseMessage error(String idKey, JsonRpcError error) {
            return new JsonRpcResponseMessage(idKey, null, error);
        }

    }

}
