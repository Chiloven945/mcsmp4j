package top.chiloven.mcsmp4j;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Factory for the Jackson mapper used by mcsmp4j.
 *
 * <p>The mapper produced by {@link #create()} is the supported baseline for serializing requests, parsing responses,
 * and
 * decoding notifications. It is configured for the protocol model classes in {@link top.chiloven.mcsmp4j.model},
 * including record support, Jackson annotations, lowercase enum values, ISO-8601 instants, and the custom scalar
 * behavior of {@link top.chiloven.mcsmp4j.model.GameRuleValue}.</p>
 *
 * <p>Most applications should use the default mapper indirectly through {@link McsmpClientConfig.Builder}. Create a
 * custom
 * mapper only when integrating with an existing object-mapping policy, adding extension payload models, or testing
 * exact JSON shapes. If a custom mapper is used, make sure it keeps compatibility with Jackson annotations on the
 * public model records; otherwise typed API calls may serialize or deserialize incorrectly.</p>
 */
public final class McsmpObjectMapper {

    private McsmpObjectMapper() {
    }

    /**
     * Creates a new mapper suitable for serializing requests and deserializing responses for MCSMP.
     *
     * @return a fresh Jackson mapper instance configured for mcsmp4j protocol models
     */
    public static ObjectMapper create() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
    }

}
