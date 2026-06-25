package top.chiloven.mcsmp4j;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Factory for the ObjectMapper used by mcsmp4j.
 */
public final class McsmpObjectMapper {

    private McsmpObjectMapper() {
    }

    public static ObjectMapper create() {
        return JsonMapper.builder().build();
    }

}
