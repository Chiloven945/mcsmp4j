package top.chiloven.mcsmp4j.internal.api;

import top.chiloven.mcsmp4j.RawApi;
import top.chiloven.mcsmp4j.api.ServerApi;
import top.chiloven.mcsmp4j.model.ServerState;
import top.chiloven.mcsmp4j.model.SystemMessage;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class ServerApiImpl implements ServerApi {

    private static final String BASE = "minecraft:server";

    private final RawApi raw;

    public ServerApiImpl(RawApi raw) {
        this.raw = requireNonNull(raw, "raw");
    }

    @Override
    public CompletableFuture<ServerState> status() {
        return raw.call(BASE + "/status", ServerState.class);
    }

    @Override
    public CompletableFuture<Boolean> save(boolean flush) {
        return raw.call(BASE + "/save", Boolean.class, flush);
    }

    @Override
    public CompletableFuture<Boolean> stop() {
        return raw.call(BASE + "/stop", Boolean.class);
    }

    @Override
    public CompletableFuture<Boolean> systemMessage(SystemMessage message) {
        return raw.call(BASE + "/system_message", Boolean.class, requireNonNull(message, "message"));
    }

}
