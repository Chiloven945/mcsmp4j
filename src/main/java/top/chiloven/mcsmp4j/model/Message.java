package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Localized or literal text payload used by system messages and kick messages.
 *
 * <p>MCSMP messages can be plain literal text or translation-key based messages. Literal messages are easiest for
 * management tools that already have final text. Translatable messages are useful when a tool wants Minecraft clients
 * to resolve a translation key using the player's language resources.</p>
 *
 * <h2>Literal messages</h2>
 *
 * <p>{@link #literal(String)} creates a message that serializes with literal text. Use it for operator-entered chat,
 * maintenance notices, and simple disconnect reasons.</p>
 *
 * <h2>Translatable messages</h2>
 *
 * <p>{@link #translatable(String, String...)} creates a message with a translation key and string parameters. The key
 * and
 * parameters are transported to the server; the client or server-side presentation layer determines how localization is
 * performed. Use stable Minecraft translation keys only when you know the target server/client versions support
 * them.</p>
 *
 * @param translatable       optional translation key
 * @param translatableParams ordered parameters for the translation key
 * @param literal            optional literal text
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Message(
        @Nullable String translatable,
        List<String> translatableParams,
        @Nullable String literal
) {

    /**
     * Validates the translation key and defensively copies translation parameters.
     */
    public Message {
        if (translatable != null && translatable.isBlank()) {
            throw new IllegalArgumentException("translatable must not be blank");
        }
        translatableParams = translatableParams == null
                ? List.of()
                : List.copyOf(translatableParams);
    }

    /**
     * Creates an empty message object.
     *
     * @return a message with no literal text and no translation key
     */
    public static Message empty() {
        return new Message(null, List.of(), null);
    }

    /**
     * Creates a literal text message.
     *
     * @param text the literal text to send
     *
     * @return a literal message
     */
    public static Message literal(String text) {
        return new Message(null, List.of(), requireNonNull(text, "text"));
    }

    /**
     * Creates a translatable message.
     *
     * @param key    the translation key
     * @param params optional translation parameters
     *
     * @return a translatable message
     */
    public static Message translatable(String key, String... params) {
        requireNonNull(params, "params");
        return new Message(requireNonNull(key, "key"), Arrays.asList(params), null);
    }

    /**
     * Returns whether this message has neither literal text nor a translation key.
     *
     * @return {@code true} for an empty message
     */
    public boolean isEmpty() {
        return translatable == null && literal == null;
    }

}
