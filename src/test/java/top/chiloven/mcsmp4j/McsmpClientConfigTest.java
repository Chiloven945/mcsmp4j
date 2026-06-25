package top.chiloven.mcsmp4j;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class McsmpClientConfigTest {

    @Test
    void buildsMinimalWsConfig() {
        var config = McsmpClientConfig.builder()
                .endpoint(URI.create("ws://localhost:25585"))
                .requestTimeout(Duration.ofSeconds(5))
                .build();

        assertThat(config.endpoint()).isEqualTo(URI.create("ws://localhost:25585"));
        assertThat(config.requestTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(config.auth()).isSameAs(McsmpAuth.none());
    }

    @Test
    void rejectsUnsupportedEndpointScheme() {
        assertThatThrownBy(() -> McsmpClientConfig.builder().endpoint(URI.create("http://localhost:25585")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ws or wss");
    }

    @Test
    void rejectsBlankBearerSecret() {
        assertThatThrownBy(() -> McsmpAuth.bearer("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("secret");
    }

}
