package top.chiloven.mcsmp4j.internal.discovery;

import org.junit.jupiter.api.Test;
import top.chiloven.mcsmp4j.McsmpObjectMapper;
import top.chiloven.mcsmp4j.version.McsmpFeature;
import top.chiloven.mcsmp4j.version.McsmpProtocolVersion;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

final class McsmpCapabilitiesParserTest {

    @Test
    void parsesProtocolVersionMethodsNotificationsAndFeatures() throws Exception {
        var schema = McsmpObjectMapper.create().readTree("""
                {
                  "protocolVersion": "3.1.0",
                  "methods": [
                    "minecraft:server/status",
                    {"name":"minecraft:gamerules/update"}
                  ],
                  "notifications": {
                    "minecraft:notification/server/activity": {},
                    "minecraft:notification/world/upgrade_progress": {}
                  }
                }
                """);

        var capabilities = McsmpDiscoveryImpl.parse(schema);

        assertThat(capabilities.protocolVersion()).contains(McsmpProtocolVersion.V3_1_0);
        assertThat(capabilities.supportsMethod("minecraft:server/status")).isTrue();
        assertThat(capabilities.supportsMethod("rpc.discover")).isTrue();
        assertThat(capabilities.supportsNotification("minecraft:notification/world/upgrade_progress")).isTrue();
        assertThat(capabilities.supports(McsmpFeature.PRE_START_DISCOVERY)).isTrue();
        assertThat(capabilities.supports(McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS)).isTrue();
        assertThat(capabilities.supports(McsmpFeature.TYPED_GAMERULE_VALUE)).isTrue();
    }

    @Test
    void infersNotificationsFromStringLeavesWhenSchemaShapeChanges() throws Exception {
        var schema = McsmpObjectMapper.create().readTree("""
                {"anything":["minecraft:notification/players/joined","minecraft:players"]}
                """);

        var capabilities = McsmpDiscoveryImpl.parse(schema);

        assertThat(capabilities.supportsNotification("minecraft:notification/players/joined")).isTrue();
        assertThat(capabilities.supportsMethod("minecraft:players")).isTrue();
    }

    @Test
    void parsesStoredProtocolContractSchemas() throws Exception {
        assertContract("discover-v1.1.0.json", McsmpProtocolVersion.V1_1_0, McsmpFeature.SERVER_ACTIVITY_NOTIFICATION);
        assertContract("discover-v2.0.0.json", McsmpProtocolVersion.V2_0_0, McsmpFeature.TYPED_GAMERULE_VALUE);
        assertContract("discover-v3.0.0.json", McsmpProtocolVersion.V3_0_0, McsmpFeature.PRE_START_DISCOVERY);
        assertContract("discover-v3.1.0.json", McsmpProtocolVersion.V3_1_0, McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS);
    }

    private static void assertContract(
            String resourceName,
            McsmpProtocolVersion expectedVersion,
            McsmpFeature expectedFeature
    ) throws IOException {
        var mapper = McsmpObjectMapper.create();
        try (var input = McsmpCapabilitiesParserTest.class.getResourceAsStream("/schemas/" + resourceName)) {
            assertThat(input).as(resourceName).isNotNull();
            var capabilities = McsmpDiscoveryImpl.parse(mapper.readTree(input));

            assertThat(capabilities.protocolVersion()).contains(expectedVersion);
            assertThat(capabilities.supports(expectedFeature)).isTrue();
            assertThat(capabilities.supportsMethod("rpc.discover")).isTrue();
        }
    }

}
