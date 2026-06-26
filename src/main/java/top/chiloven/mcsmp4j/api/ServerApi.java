package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.ServerState;
import top.chiloven.mcsmp4j.model.SystemMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Strongly typed client for the {@code minecraft:server} method group.
 *
 * <p>This group covers server lifecycle status, save and stop requests, and broadcast or targeted system
 * messages. These operations affect the whole dedicated server rather than a single list resource.</p>
 */
public interface ServerApi {

    /**
     * Gets the current server state.
     *
     * <p>On newer protocol versions, the management endpoint can be available before the dedicated server has
     * fully started; in that case {@link ServerState#started()} may be {@code false}.</p>
     *
     * @return a future containing the current server state snapshot
     */
    CompletableFuture<ServerState> status();

    /**
     * Starts a normal save without forcing an immediate flush.
     *
     * @return a future containing whether the server accepted or started the save operation
     */
    default CompletableFuture<Boolean> save() {
        return save(false);
    }

    /**
     * Requests a server save.
     *
     * @param flush whether the server should flush data to disk immediately as part of the save
     *
     * @return a future containing whether the server accepted or started the save operation
     */
    CompletableFuture<Boolean> save(boolean flush);

    /**
     * Requests a graceful server stop.
     *
     * @return a future containing whether the server accepted or started stopping
     */
    CompletableFuture<Boolean> stop();

    /**
     * Sends a literal chat message to players as a system message.
     *
     * @param text the text to send
     *
     * @return a future containing whether the server sent the message
     */
    default CompletableFuture<Boolean> chat(String text) {
        return systemMessage(SystemMessage.chat(Message.literal(text)));
    }

    /**
     * Sends a system message, either to all players or to selected recipients.
     *
     * @param message the message payload and delivery options
     *
     * @return a future containing whether the server sent the message
     */
    CompletableFuture<Boolean> systemMessage(SystemMessage message);

    /**
     * Sends a literal action-bar overlay message to players.
     *
     * @param text the text to send as an overlay
     *
     * @return a future containing whether the server sent the message
     */
    default CompletableFuture<Boolean> actionBar(String text) {
        return systemMessage(SystemMessage.actionBar(Message.literal(text)));
    }

}
