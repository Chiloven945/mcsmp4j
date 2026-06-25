package top.chiloven.mcsmp4j;

import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import top.chiloven.mcsmp4j.version.McsmpVersionPolicy;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Immutable connection configuration for {@link McsmpClient}.
 */
public final class McsmpClientConfig {

    private final URI endpoint;
    private final McsmpAuth auth;
    private final @Nullable String origin;
    private final Duration connectTimeout;
    private final Duration requestTimeout;
    private final @Nullable SSLContext sslContext;
    private final Map<String, String> headers;
    private final ObjectMapper objectMapper;
    private final boolean legacyNotificationPrefix;
    private final McsmpVersionPolicy versionPolicy;

    private McsmpClientConfig(
            Builder builder
    ) {
        this.endpoint = requireNonNull(builder.endpoint, "endpoint");
        this.auth = requireNonNull(builder.auth, "auth");
        this.origin = builder.origin;
        this.connectTimeout = positive(builder.connectTimeout, "connectTimeout");
        this.requestTimeout = positive(builder.requestTimeout, "requestTimeout");
        this.sslContext = builder.sslContext;
        this.headers = Map.copyOf(builder.headers);
        this.objectMapper = requireNonNull(builder.objectMapper, "objectMapper");
        this.legacyNotificationPrefix = builder.legacyNotificationPrefix
                || builder.versionPolicy == McsmpVersionPolicy.COMPATIBLE;
        this.versionPolicy = requireNonNull(builder.versionPolicy, "versionPolicy");
    }

    private static Duration positive(Duration duration, String name) {
        requireNonNull(duration, name);
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public URI endpoint() {
        return endpoint;
    }

    public McsmpAuth auth() {
        return auth;
    }

    public @Nullable String origin() {
        return origin;
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    public Duration requestTimeout() {
        return requestTimeout;
    }

    public @Nullable SSLContext sslContext() {
        return sslContext;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    public boolean legacyNotificationPrefix() {
        return legacyNotificationPrefix;
    }

    public McsmpVersionPolicy versionPolicy() {
        return versionPolicy;
    }

    public static final class Builder {

        private final Map<String, String> headers = new LinkedHashMap<>();
        private @Nullable URI endpoint;
        private McsmpAuth auth = McsmpAuth.none();
        private @Nullable String origin;
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration requestTimeout = Duration.ofSeconds(30);
        private @Nullable SSLContext sslContext;
        private ObjectMapper objectMapper = McsmpObjectMapper.create();
        private boolean legacyNotificationPrefix;
        private McsmpVersionPolicy versionPolicy = McsmpVersionPolicy.COMPATIBLE;

        private Builder() {
        }

        public Builder endpoint(URI endpoint) {
            this.endpoint = requireNonNull(endpoint, "endpoint");
            var scheme = endpoint.getScheme();
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                throw new IllegalArgumentException("endpoint scheme must be ws or wss");
            }
            return this;
        }

        public Builder secret(String secret) {
            return auth(McsmpAuth.bearer(secret));
        }

        public Builder auth(McsmpAuth auth) {
            this.auth = requireNonNull(auth, "auth");
            return this;
        }

        public Builder origin(@Nullable String origin) {
            if (origin != null && origin.isBlank()) {
                throw new IllegalArgumentException("origin must not be blank");
            }
            this.origin = origin;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = positive(connectTimeout, "connectTimeout");
            return this;
        }

        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = positive(requestTimeout, "requestTimeout");
            return this;
        }

        public Builder sslContext(@Nullable SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder header(String name, String value) {
            requireNonNull(name, "name");
            requireNonNull(value, "value");
            if (name.isBlank()) {
                throw new IllegalArgumentException("header name must not be blank");
            }
            headers.put(name, value);
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = requireNonNull(objectMapper, "objectMapper");
            return this;
        }

        public Builder legacyNotificationPrefix(boolean legacyNotificationPrefix) {
            this.legacyNotificationPrefix = legacyNotificationPrefix;
            return this;
        }

        public Builder versionPolicy(McsmpVersionPolicy versionPolicy) {
            this.versionPolicy = requireNonNull(versionPolicy, "versionPolicy");
            return this;
        }

        public McsmpClientConfig build() {
            return new McsmpClientConfig(this);
        }

    }

}
