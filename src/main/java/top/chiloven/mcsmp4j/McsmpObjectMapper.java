package top.chiloven.mcsmp4j;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Factory for the Jackson {@link ObjectMapper} configuration used by mcsmp4j.
 *
 * <p>The mapper is configured for stable protocol mapping rather than application-specific preferences. It
 * understands Java records, keeps enum handling strict, and ignores unknown properties so that clients can keep working
 * when newer servers add fields to existing JSON objects. Applications that provide their own mapper via
 * {@link McsmpClientConfig.Builder#objectMapper(ObjectMapper)} should preserve these compatibility properties unless
 * they intentionally want stricter behavior.</p>
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
