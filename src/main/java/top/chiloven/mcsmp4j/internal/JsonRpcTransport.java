package top.chiloven.mcsmp4j.internal;

import tools.jackson.databind.JsonNode;
import top.chiloven.mcsmp4j.*;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotificationListener;
import top.chiloven.mcsmp4j.protocol.JsonRpcSubscription;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public final class JsonRpcTransport implements AutoCloseable {

    private static final int NORMAL_CLOSURE = 1000;

    private final McsmpClientConfig config;
    private final JsonRpcCodec codec;
    private final NotificationDispatcher notifications = new NotificationDispatcher();
    private final AtomicReference<State> state = new AtomicReference<>(State.NEW);
    private final AtomicLong nextId = new AtomicLong(1);
    private final ConcurrentHashMap<String, CompletableFuture<JsonNode>> pending = new ConcurrentHashMap<>();
    private final StringBuilder inboundText = new StringBuilder();

    private volatile WebSocket webSocket;

    public JsonRpcTransport(
            McsmpClientConfig config
    ) {
        this.config = requireNonNull(config, "config");
        this.codec = new JsonRpcCodec(config.objectMapper());
    }

    public CompletableFuture<Void> connect() {
        if (!state.compareAndSet(State.NEW, State.CONNECTING)) {
            return failedFuture(new McsmpConnectionException("MCSMP client is already used"));
        }

        var httpClientBuilder = HttpClient.newBuilder()
                .connectTimeout(config.connectTimeout());
        if (config.sslContext() != null) {
            httpClientBuilder.sslContext(config.sslContext());
        }

        var builder = httpClientBuilder.build()
                .newWebSocketBuilder()
                .connectTimeout(config.connectTimeout());

        if (config.origin() != null) {
            builder.header("Origin", config.origin());
        }
        for (var header : config.headers().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
        config.auth().apply(builder);

        return builder.buildAsync(config.endpoint(), new Listener())
                .handle((socket, error) -> {
                    if (error != null) {
                        state.set(State.FAILED);
                        throw new McsmpConnectionException("Could not connect to MCSMP endpoint " + config.endpoint(), unwrap(error));
                    }
                    webSocket = socket;
                    state.set(State.CONNECTED);
                    return null;
                });
    }

    private static <T> CompletableFuture<T> failedFuture(Throwable error) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }

    private static Throwable unwrap(Throwable error) {
        if (error instanceof CompletionException completionException && completionException.getCause() != null) {
            return completionException.getCause();
        }
        if (error instanceof ExecutionException executionException && executionException.getCause() != null) {
            return executionException.getCause();
        }
        return error;
    }

    public boolean isConnected() {
        return state.get() == State.CONNECTED;
    }

    public CompletableFuture<JsonNode> call(String method, Object... params) {
        requireNonNull(method, "method");
        requireNonNull(params, "params");
        var socket = webSocket;
        if (state.get() != State.CONNECTED || socket == null) {
            return failedFuture(new McsmpConnectionException("MCSMP client is not connected"));
        }

        var id = nextId.getAndIncrement();
        var idKey = Long.toString(id);
        CompletableFuture<JsonNode> response = new CompletableFuture<>();
        pending.put(idKey, response);

        var timeout = config.requestTimeout();
        var visible = response.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionallyCompose(error -> {
                    var unwrapped = unwrap(error);
                    if (unwrapped instanceof TimeoutException) {
                        return failedFuture(new McsmpTimeoutException("MCSMP request timed out after " + timeout, unwrapped));
                    }
                    return failedFuture(unwrapped);
                });
        visible.whenComplete((ignored, ignoredError) -> pending.remove(idKey));

        String payload;
        try {
            payload = codec.encodeRequest(id, method, params);
        } catch (RuntimeException e) {
            pending.remove(idKey);
            return failedFuture(e);
        }

        socket.sendText(payload, true).whenComplete((ignoredSocket, sendError) -> {
            if (sendError != null) {
                response.completeExceptionally(new McsmpConnectionException("Could not send MCSMP request", unwrap(sendError)));
            }
        });

        return visible;
    }

    public JsonRpcSubscription subscribe(JsonRpcNotificationListener listener) {
        return notifications.subscribe(listener);
    }

    @Override
    public void close() {
        closeAsync().join();
    }

    public CompletableFuture<Void> closeAsync() {
        var current = state.get();
        if (current == State.CLOSED || current == State.CLOSING) {
            return CompletableFuture.completedFuture(null);
        }
        state.set(State.CLOSING);
        failPending(new McsmpConnectionException("MCSMP client is closing"));

        var socket = webSocket;
        if (socket == null) {
            state.set(State.CLOSED);
            return CompletableFuture.completedFuture(null);
        }
        return socket.sendClose(NORMAL_CLOSURE, "mcsmp4j closing")
                .handle((ignored, error) -> {
                    state.set(State.CLOSED);
                    if (error != null) {
                        throw new McsmpConnectionException("Could not close MCSMP WebSocket", unwrap(error));
                    }
                    return null;
                });
    }

    private void failPending(Throwable error) {
        for (var future : pending.values()) {
            future.completeExceptionally(error);
        }
        pending.clear();
    }

    private void onText(CharSequence data, boolean last) {
        String payload;
        synchronized (inboundText) {
            inboundText.append(data);
            if (!last) {
                return;
            }
            payload = inboundText.toString();
            inboundText.setLength(0);
        }

        var message = codec.decode(payload);
        switch (message) {
            case JsonRpcCodec.ParsedMessage.Response response -> handleResponse(response.response());
            case JsonRpcCodec.ParsedMessage.Notification notification ->
                    notifications.dispatch(notification.notification());
        }
    }

    private void handleResponse(JsonRpcCodec.JsonRpcResponseMessage response) {
        var future = pending.remove(response.idKey());
        if (future == null) {
            return;
        }
        if (response.error() != null) {
            future.completeExceptionally(new McsmpRemoteException(
                    response.error().code(),
                    response.error().message(),
                    response.error().data()
            ));
            return;
        }
        if (response.result() == null) {
            future.completeExceptionally(new McsmpProtocolException("JSON-RPC response did not contain result or error"));
            return;
        }
        future.complete(response.result());
    }

    private enum State {
        NEW,
        CONNECTING,
        CONNECTED,
        CLOSING,
        CLOSED,
        FAILED
    }

    private final class Listener implements WebSocket.Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(
                WebSocket webSocket,
                CharSequence data,
                boolean last
        ) {
            try {
                JsonRpcTransport.this.onText(data, last);
            } catch (RuntimeException e) {
                state.set(State.FAILED);
                failPending(e);
                webSocket.abort();
            } finally {
                webSocket.request(1);
            }
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(
                WebSocket webSocket,
                int statusCode,
                String reason
        ) {
            state.set(State.CLOSED);
            failPending(new McsmpConnectionException("MCSMP WebSocket closed: " + statusCode + " " + reason));
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(
                WebSocket webSocket,
                Throwable error
        ) {
            state.set(State.FAILED);
            failPending(new McsmpConnectionException("MCSMP WebSocket failed", error));
        }

    }

}
