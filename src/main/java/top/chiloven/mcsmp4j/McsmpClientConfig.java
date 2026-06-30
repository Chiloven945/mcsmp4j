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
 * Immutable configuration used to create an {@link McsmpClient}.
 *
 * <p>This object captures every value needed for the opening WebSocket handshake and for local protocol processing:
 * endpoint URI, authentication strategy, optional {@code Origin} header, connection and request timeouts, optional TLS
 * customization, extra HTTP headers, the Jackson mapper, notification-prefix compatibility, and version-policy
 * behavior. It is intentionally immutable so it can be stored, reused, logged, or passed between application components
 * without being changed unexpectedly.</p>
 *
 * <h2>Endpoint and TLS</h2>
 *
 * <p>The endpoint must use {@code ws://} or {@code wss://}. Production Minecraft servers commonly enable TLS for the
 * management endpoint, so {@code wss://} is usually the appropriate scheme. When the server uses a self-signed
 * certificate or a private certificate authority, configure {@link Builder#sslContext(javax.net.ssl.SSLContext)} with
 * an SSL context that trusts that certificate. The library does not disable certificate validation automatically.</p>
 *
 * <h2>Authentication and origin</h2>
 *
 * <p>MCSMP servers require the configured management secret. {@link Builder#secret(String)} configures the common
 * bearer
 * form. {@link Builder#auth(McsmpAuth)} can be used when an application needs the WebSocket subprotocol form. Some
 * servers also require an {@code Origin} header listed in {@code management-server-allowed-origins}; use
 * {@link Builder#origin(String)} for that value.</p>
 *
 * <h2>Timeouts</h2>
 *
 * <p>{@link #connectTimeout()} limits the WebSocket opening handshake. {@link #requestTimeout()} applies to every
 * individual JSON-RPC request after it is sent. Long-running operations such as server saves should be given enough
 * time for the server to answer. A timeout does not necessarily mean the server did not perform the action; it means
 * the client did not receive the corresponding JSON-RPC response within the configured window.</p>
 *
 * <h2>JSON mapper</h2>
 *
 * <p>The default mapper returned by {@link McsmpObjectMapper#create()} is configured for the model types shipped with
 * this
 * library. Supplying a custom mapper is advanced usage. If you replace it, keep the same Jackson modules and annotation
 * behavior unless you fully control both serialization and deserialization of all request/response/notification
 * payloads.</p>
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

    /**
     * Starts building a client configuration.
     *
     * @return a new mutable builder with mcsmp4j defaults
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the WebSocket endpoint URI.
     *
     * <p>The URI scheme must be {@code ws} or {@code wss}. The path is usually empty for vanilla servers because
     * the MCSMP endpoint is exposed directly on the configured management host and port.</p>
     *
     * @return the management WebSocket endpoint
     */
    public URI endpoint() {
        return endpoint;
    }

    /**
     * Returns the authentication strategy applied during the WebSocket handshake.
     *
     * @return the configured authentication strategy
     */
    public McsmpAuth auth() {
        return auth;
    }

    /**
     * Returns the optional HTTP {@code Origin} header value.
     *
     * <p>Minecraft servers may reject clients whose origin is not included in
     * {@code management-server-allowed-origins}. The string does not need to be a URL; it only needs to match the
     * server configuration.</p>
     *
     * @return the origin header value, or {@code null} if no origin header should be sent
     */
    public @Nullable String origin() {
        return origin;
    }

    /**
     * Returns the maximum duration allowed for the WebSocket opening handshake.
     *
     * @return the connection timeout
     */
    public Duration connectTimeout() {
        return connectTimeout;
    }

    /**
     * Returns the per-request timeout for JSON-RPC calls.
     *
     * @return the request timeout applied to each pending call
     */
    public Duration requestTimeout() {
        return requestTimeout;
    }

    /**
     * Returns the optional SSL context used for {@code wss} connections.
     *
     * <p>Provide this when connecting to a server that uses a self-signed certificate or a private certificate
     * authority. When absent, the JDK default SSL context is used.</p>
     *
     * @return the custom SSL context, or {@code null} for the JDK default
     */
    public @Nullable SSLContext sslContext() {
        return sslContext;
    }

    /**
     * Returns additional HTTP headers added to the WebSocket opening handshake.
     *
     * @return an immutable map of header names to header values
     */
    public Map<String, String> headers() {
        return headers;
    }

    /**
     * Returns the Jackson mapper used for protocol serialization and deserialization.
     *
     * @return the configured object mapper
     */
    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    /**
     * Returns whether local event dispatch accepts the pre-release legacy notification prefix.
     *
     * <p>When enabled, notifications using the old {@code notification:*} form are normalized to the modern
     * {@code minecraft:notification/*} form before typed event decoding.</p>
     *
     * @return {@code true} if legacy notification prefix compatibility is enabled
     */
    public boolean legacyNotificationPrefix() {
        return legacyNotificationPrefix;
    }

    /**
     * Returns the configured protocol-version compatibility policy.
     *
     * @return the version policy used by the client configuration
     */
    public McsmpVersionPolicy versionPolicy() {
        return versionPolicy;
    }

    /**
     * Mutable builder for {@link McsmpClientConfig}.
     *
     * <p>The builder performs validation as values are assigned where possible. {@link #build()} validates that an
     * endpoint has been supplied and returns an immutable configuration.</p>
     */
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

        /**
         * Sets the management WebSocket endpoint.
         *
         * @param endpoint a {@code ws://} or {@code wss://} URI for the Minecraft management server
         *
         * @return this builder
         *
         * @throws NullPointerException     if {@code endpoint} is {@code null}
         * @throws IllegalArgumentException if the URI scheme is not {@code ws} or {@code wss}
         */
        public Builder endpoint(URI endpoint) {
            this.endpoint = requireNonNull(endpoint, "endpoint");
            var scheme = endpoint.getScheme();
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                throw new IllegalArgumentException("endpoint scheme must be ws or wss");
            }
            return this;
        }

        /**
         * Configures bearer-token authentication using the server management secret.
         *
         * <p>This is a convenience method for {@code auth(McsmpAuth.bearer(secret))}.</p>
         *
         * @param secret the non-blank management secret
         *
         * @return this builder
         */
        public Builder secret(String secret) {
            return auth(McsmpAuth.bearer(secret));
        }

        /**
         * Sets the authentication strategy used during the WebSocket handshake.
         *
         * @param auth the authentication strategy to use
         *
         * @return this builder
         *
         * @throws NullPointerException if {@code auth} is {@code null}
         */
        public Builder auth(McsmpAuth auth) {
            this.auth = requireNonNull(auth, "auth");
            return this;
        }

        /**
         * Sets the optional HTTP {@code Origin} header.
         *
         * @param origin the origin value expected by the server, or {@code null} to omit the header
         *
         * @return this builder
         *
         * @throws IllegalArgumentException if {@code origin} is blank
         */
        public Builder origin(@Nullable String origin) {
            if (origin != null && origin.isBlank()) {
                throw new IllegalArgumentException("origin must not be blank");
            }
            this.origin = origin;
            return this;
        }

        /**
         * Sets the timeout for opening the WebSocket connection.
         *
         * @param connectTimeout a positive duration
         *
         * @return this builder
         *
         * @throws NullPointerException     if {@code connectTimeout} is {@code null}
         * @throws IllegalArgumentException if the duration is zero or negative
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = positive(connectTimeout, "connectTimeout");
            return this;
        }

        /**
         * Sets the timeout applied to each JSON-RPC request.
         *
         * @param requestTimeout a positive duration
         *
         * @return this builder
         *
         * @throws NullPointerException     if {@code requestTimeout} is {@code null}
         * @throws IllegalArgumentException if the duration is zero or negative
         */
        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = positive(requestTimeout, "requestTimeout");
            return this;
        }

        /**
         * Sets the optional SSL context for secure WebSocket connections.
         *
         * @param sslContext the SSL context to use for {@code wss://} endpoints, or {@code null} for the JDK default
         *
         * @return this builder
         */
        public Builder sslContext(@Nullable SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        /**
         * Adds or replaces a custom HTTP header for the WebSocket opening handshake.
         *
         * @param name  the non-blank header name
         * @param value the header value
         *
         * @return this builder
         *
         * @throws NullPointerException     if {@code name} or {@code value} is {@code null}
         * @throws IllegalArgumentException if {@code name} is blank
         */
        public Builder header(String name, String value) {
            requireNonNull(name, "name");
            requireNonNull(value, "value");
            if (name.isBlank()) {
                throw new IllegalArgumentException("header name must not be blank");
            }
            headers.put(name, value);
            return this;
        }

        /**
         * Sets the Jackson mapper used for all protocol JSON mapping.
         *
         * @param objectMapper the mapper to use
         *
         * @return this builder
         *
         * @throws NullPointerException if {@code objectMapper} is {@code null}
         */
        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = requireNonNull(objectMapper, "objectMapper");
            return this;
        }

        /**
         * Enables or disables compatibility with the legacy notification prefix.
         *
         * <p>This flag is automatically treated as enabled when {@link #versionPolicy(McsmpVersionPolicy)} is set
         * to {@link McsmpVersionPolicy#COMPATIBLE}.</p>
         *
         * @param legacyNotificationPrefix whether to accept legacy notification method names locally
         *
         * @return this builder
         */
        public Builder legacyNotificationPrefix(boolean legacyNotificationPrefix) {
            this.legacyNotificationPrefix = legacyNotificationPrefix;
            return this;
        }

        /**
         * Sets the high-level compatibility policy used by the client.
         *
         * @param versionPolicy the policy to use
         *
         * @return this builder
         *
         * @throws NullPointerException if {@code versionPolicy} is {@code null}
         */
        public Builder versionPolicy(McsmpVersionPolicy versionPolicy) {
            this.versionPolicy = requireNonNull(versionPolicy, "versionPolicy");
            return this;
        }

        /**
         * Builds an immutable client configuration.
         *
         * @return the finished configuration
         *
         * @throws NullPointerException if no endpoint has been configured
         */
        public McsmpClientConfig build() {
            return new McsmpClientConfig(this);
        }

    }

}
