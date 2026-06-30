package top.chiloven.mcsmp4j.api;

import top.chiloven.mcsmp4j.model.Message;
import top.chiloven.mcsmp4j.model.ServerState;
import top.chiloven.mcsmp4j.model.SystemMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Type-safe client for the {@code minecraft:server} method group.
 *
 * <p>This interface contains lifecycle and messaging operations for the dedicated server itself: reading status,
 * starting a
 * save, requesting shutdown, and broadcasting system messages. These operations can have visible side effects for every
 * player on the server, so management applications should present confirmation prompts for save/stop/message actions
 * when used interactively.</p>
 *
 * <h2>Protocol mapping</h2>
 *
 * <ul>
 *     <li>{@link #status()} maps to {@code minecraft:server/status}</li>
 *     <li>{@link #save(boolean)} maps to {@code minecraft:server/save}</li>
 *     <li>{@link #stop()} maps to {@code minecraft:server/stop}</li>
 *     <li>{@link #systemMessage(top.chiloven.mcsmp4j.model.SystemMessage)} maps to {@code minecraft:server/system_message}</li>
 * </ul>
 *
 * <p>In newer protocol versions the management endpoint may be reachable before the Minecraft server has fully started.
 * When {@link top.chiloven.mcsmp4j.model.ServerState#started()} is {@code false}, only a subset of operations may be
 * available. Use discovery and status polling to build startup-aware dashboards.</p>
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
