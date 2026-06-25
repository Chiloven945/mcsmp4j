package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.ServerState;
import top.chiloven.mcsmp4j.model.SystemMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Strongly-typed client for {@code minecraft:server/*} methods.
 */
public interface ServerApi {

    CompletableFuture<ServerState> status();

    default CompletableFuture<Boolean> save() {
        return save(false);
    }

    CompletableFuture<Boolean> save(boolean flush);

    CompletableFuture<Boolean> stop();

    default CompletableFuture<Boolean> chat(String text) {
        return systemMessage(SystemMessage.chat(Message.literal(text)));
    }

    CompletableFuture<Boolean> systemMessage(SystemMessage message);

    default CompletableFuture<Boolean> actionBar(String text) {
        return systemMessage(SystemMessage.actionBar(Message.literal(text)));
    }

}
