package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Display message supporting either a literal string or a translation key with parameters.
 *
 * <p>The protocol message schema can represent localized messages by translation key or simple literal text.
 * Literal messages are easiest for most administration tools. Translation messages are useful when a client wants the
 * Minecraft client to localize a known translation key.</p>
 *
 * @param translatable       optional translation key
 * @param translatableParams parameters substituted into the translation key
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
