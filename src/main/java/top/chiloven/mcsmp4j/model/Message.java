package top.chiloven.mcsmp4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Display message supporting either a literal string or a translation key with parameters.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Message(
        @Nullable String translatable,
        List<String> translatableParams,
        @Nullable String literal
) {

    public Message {
        if (translatable != null && translatable.isBlank()) {
            throw new IllegalArgumentException("translatable must not be blank");
        }
        translatableParams = translatableParams == null
                ? List.of()
                : List.copyOf(translatableParams);
    }

    public static Message empty() {
        return new Message(null, List.of(), null);
    }

    public static Message literal(String text) {
        return new Message(null, List.of(), requireNonNull(text, "text"));
    }

    public static Message translatable(String key, String... params) {
        requireNonNull(params, "params");
        return new Message(requireNonNull(key, "key"), Arrays.asList(params), null);
    }

    public boolean isEmpty() {
        return translatable == null && literal == null;
    }

}
