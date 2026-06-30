package top.chiloven.mcsmp4j;

import top.chiloven.mcsmp4j.api.*;
import top.chiloven.mcsmp4j.discovery.McsmpCapabilities;
import top.chiloven.mcsmp4j.discovery.McsmpDiscovery;
import top.chiloven.mcsmp4j.event.McsmpEvents;
import top.chiloven.mcsmp4j.internal.JsonRpcTransport;
import top.chiloven.mcsmp4j.internal.RawApiImpl;
import top.chiloven.mcsmp4j.internal.api.*;
import top.chiloven.mcsmp4j.internal.discovery.McsmpDiscoveryImpl;
import top.chiloven.mcsmp4j.internal.event.McsmpEventDecoder;
import top.chiloven.mcsmp4j.internal.event.McsmpEventsImpl;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotificationListener;
import top.chiloven.mcsmp4j.protocol.JsonRpcSubscription;
import top.chiloven.mcsmp4j.version.McsmpVersionPolicy;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

/**
 * Primary entry point for connecting to and controlling an MCSMP endpoint.
 *
 * <p>An {@code McsmpClient} represents exactly one WebSocket connection to a Minecraft dedicated server's management
 * endpoint. It owns the JSON-RPC request id sequence, the set of pending request futures, the raw notification stream,
 * and all typed API facade objects exposed by the client. Applications normally create one client per management
 * server.</p>
 *
 * <h2>Layered API design</h2>
 *
 * <p>The client exposes three layers that can be mixed freely:</p>
 *
 * <ul>
 *     <li><strong>Typed official APIs</strong>, such as {@link #server()}, {@link #players()}, {@link #allowlist()},
 *     {@link #serverSettings()}, and {@link #gamerules()}, for methods in the official {@code minecraft:*} namespace.</li>
 *     <li><strong>Raw JSON-RPC calls</strong> through {@link #raw()} for custom namespaces, modded servers, diagnostic
 *     tools, or protocol features that are newer than this library release.</li>
 *     <li><strong>Notification listeners</strong> through {@link #events()} for typed events and
 *     {@link #onNotification(JsonRpcNotificationListener)} for untyped JSON-RPC notifications.</li>
 * </ul>
 *
 * <h2>Connection lifecycle</h2>
 *
 * <p>A client created by {@link #builder()} or {@link #create(McsmpClientConfig)} is initially disconnected. Call
 * {@link #connect()} before sending requests or relying on notifications. A connected client remains usable until the
 * remote endpoint closes, the local application calls {@link #closeAsync()} or {@link #close()}, or an unrecoverable
 * transport error occurs. This type does not currently implement automatic reconnect because reconnect policy is highly
 * application-specific: some tools should fail fast, some should back off, and some should rediscover capabilities after a
 * server restart.</p>
 *
 * <h2>Asynchronous programming model</h2>
 *
 * <p>All server operations return {@link java.util.concurrent.CompletableFuture}. The future completes on response, on
 * request timeout, or when the connection is closed before a response arrives. Callers that want blocking behavior may use
 * {@link java.util.concurrent.CompletableFuture#join()} or {@link java.util.concurrent.CompletableFuture#get()}, but GUI,
 * server plugin, and bot applications should usually compose futures instead of blocking their event loops.</p>
 *
 * <h2>Error model</h2>
 *
 * <p>Handshake rejection caused by a missing/incorrect secret or disallowed origin is reported as
 * {@link McsmpAuthenticationException}. I/O failures are reported as {@link McsmpConnectionException}. A server-side
 * JSON-RPC error response is reported as {@link McsmpRemoteException}; inspect its {@link McsmpRemoteException#code()},
 * {@link McsmpRemoteException#remoteMessage()}, and {@link McsmpRemoteException#data()} when building user-facing tools.
 * Malformed JSON-RPC messages are reported as {@link McsmpProtocolException}.</p>
 *
 * <h2>Typical usage</h2>
 *
 * <pre>{@code
 * try (McsmpClient client = McsmpClient.builder()
 *         .endpoint(URI.create("wss://localhost:25585"))
 *         .secret(System.getenv("MCSMP_SECRET"))
 *         .origin("mcsmp4j")
 *         .build()) {
 *     client.connect().join();
 *
 *     McsmpCapabilities capabilities = client.discover().join();
 *     ServerState state = client.server().status().join();
 *
 *     client.events().on(PlayerJoinedEvent.class, event ->
 *             System.out.println(event.player().displayName() + " joined"));
 * }
 * }</pre>
 *
 * <p>{@code McsmpClient} is thread-safe for typical concurrent use: multiple application threads may issue requests,
 * register listeners, and close subscriptions concurrently. Listener callbacks should still be written defensively and
 * should not assume they run on a dedicated application thread.</p>
 */
public final class McsmpClient implements AutoCloseable {

    private final McsmpClientConfig config;
    private final JsonRpcTransport transport;
    private final RawApi raw;
    private final AllowlistApi allowlist;
    private final PlayersApi players;
    private final ServerApi server;
    private final BansApi bans;
    private final IpBansApi ipBans;
    private final OperatorsApi operators;
    private final ServerSettingsApi serverSettings;
    private final GamerulesApi gamerules;
    private final McsmpDiscovery discovery;
    private final McsmpEventsImpl events;
    private volatile McsmpCapabilities capabilities;

    private McsmpClient(
            McsmpClientConfig config
    ) {
        this.config = requireNonNull(config, "config");
        this.transport = new JsonRpcTransport(config);
        this.raw = new RawApiImpl(transport, config.objectMapper());
        this.allowlist = new AllowlistApiImpl(raw);
        this.players = new PlayersApiImpl(raw);
        this.server = new ServerApiImpl(raw);
        this.bans = new BansApiImpl(raw);
        this.ipBans = new IpBansApiImpl(raw);
        this.operators = new OperatorsApiImpl(raw);
        this.serverSettings = new ServerSettingsApiImpl(raw);
        this.gamerules = new GamerulesApiImpl(raw);
        this.discovery = new McsmpDiscoveryImpl(raw);
        this.events = new McsmpEventsImpl(
                new McsmpEventDecoder(config.objectMapper(), config.legacyNotificationPrefix()),
                transport::subscribe
        );
    }

    /**
     * Creates a new fluent builder for a client.
     *
     * <p>This is the most convenient way to configure and create an {@code McsmpClient}. The returned builder
     * delegates to {@link McsmpClientConfig.Builder} and then wraps the built configuration in a client instance.</p>
     *
     * @return a new client builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates and connects a client in one asynchronous step.
     *
     * @param config the immutable client configuration
     *
     * @return a future that completes with the connected client
     */
    public static CompletableFuture<McsmpClient> connect(McsmpClientConfig config) {
        var client = create(config);
        return client.connect();
    }

    /**
     * Creates a client without opening the WebSocket connection.
     *
     * <p>Call {@link #connect()} on the returned instance before using request or notification APIs.</p>
     *
     * @param config the immutable client configuration
     *
     * @return a disconnected client configured with {@code config}
     */
    public static McsmpClient create(McsmpClientConfig config) {
        return new McsmpClient(config);
    }

    /**
     * Opens the WebSocket connection to the configured endpoint.
     *
     * <p>The future completes with this client after the WebSocket handshake succeeds. If the server rejects the
     * secret or origin, the future completes exceptionally with {@link McsmpAuthenticationException}; other transport
     * failures complete with {@link McsmpConnectionException}.</p>
     *
     * @return a future that completes with this connected client
     */
    public CompletableFuture<McsmpClient> connect() {
        return transport.connect().thenApply(ignored -> this);
    }

    /**
     * Returns whether the underlying WebSocket is currently open.
     *
     * @return {@code true} if the client is connected and able to send requests
     */
    public boolean isConnected() {
        return transport.isConnected();
    }

    /**
     * Returns the immutable configuration used by this client.
     *
     * @return the client configuration
     */
    public McsmpClientConfig config() {
        return config;
    }

    /**
     * Returns the low-level JSON-RPC API for official, future, and custom namespaces.
     *
     * @return the raw JSON-RPC caller
     */
    public RawApi raw() {
        return raw;
    }

    /**
     * Returns the allowlist API group.
     *
     * @return typed operations for {@code minecraft:allowlist}
     */
    public AllowlistApi allowlist() {
        return allowlist;
    }

    /**
     * Returns the connected-player API group.
     *
     * @return typed operations for {@code minecraft:players}
     */
    public PlayersApi players() {
        return players;
    }

    /**
     * Returns the server lifecycle and messaging API group.
     *
     * @return typed operations for {@code minecraft:server}
     */
    public ServerApi server() {
        return server;
    }

    /**
     * Returns the user-ban API group.
     *
     * @return typed operations for {@code minecraft:bans}
     */
    public BansApi bans() {
        return bans;
    }

    /**
     * Returns the IP-ban API group.
     *
     * @return typed operations for {@code minecraft:ip_bans}
     */
    public IpBansApi ipBans() {
        return ipBans;
    }

    /**
     * Returns the operator API group.
     *
     * @return typed operations for {@code minecraft:operators}
     */
    public OperatorsApi operators() {
        return operators;
    }

    /**
     * Returns the server-settings API group.
     *
     * @return typed operations for {@code minecraft:serversettings}
     */
    public ServerSettingsApi serverSettings() {
        return serverSettings;
    }

    /**
     * Returns the game-rule API group.
     *
     * @return typed operations for {@code minecraft:gamerules}
     */
    public GamerulesApi gamerules() {
        return gamerules;
    }

    /**
     * Returns the typed event registry for server notifications.
     *
     * @return the notification listener registry
     */
    public McsmpEvents events() {
        return events;
    }

    /**
     * Returns the discovery API wrapper.
     *
     * @return an object that calls {@code rpc.discover} and maps the result to capabilities
     */
    public McsmpDiscovery discovery() {
        return discovery;
    }

    /**
     * Calls {@code rpc.discover}, caches the parsed capabilities, and returns them.
     *
     * <p>Calling discovery is optional, but recommended when an application depends on functionality introduced
     * in newer protocol versions. The cached value is returned later by {@link #capabilities()}.</p>
     *
     * @return a future that completes with the discovered server capabilities
     */
    public CompletableFuture<McsmpCapabilities> discover() {
        return discovery.discover().thenApply(discovered -> {
            capabilities = discovered;
            return discovered;
        });
    }

    /**
     * Returns the last capabilities returned by {@link #discover()}.
     *
     * @return an optional containing cached capabilities, or empty if discovery has not completed successfully
     */
    public Optional<McsmpCapabilities> capabilities() {
        return Optional.ofNullable(capabilities);
    }

    /**
     * Registers a raw JSON-RPC notification listener.
     *
     * <p>This is lower-level than {@link #events()}; it receives every server notification before typed event
     * filtering. The returned subscription should be closed when the listener is no longer needed.</p>
     *
     * @param listener the listener to invoke for each raw JSON-RPC notification
     *
     * @return a subscription handle that unregisters the listener when closed
     */
    public JsonRpcSubscription onNotification(JsonRpcNotificationListener listener) {
        return transport.subscribe(listener);
    }

    /**
     * Closes the client asynchronously.
     *
     * <p>All typed event subscriptions are removed, the WebSocket close frame is sent, and pending requests are
     * completed exceptionally by the transport.</p>
     *
     * @return a future that completes when the close process has been initiated and acknowledged by the transport
     */
    public CompletableFuture<Void> closeAsync() {
        events.close();
        return transport.closeAsync();
    }

    /**
     * Closes the client and releases local resources.
     *
     * <p>This method is safe to call more than once. It does not wait for outstanding requests to finish; pending
     * requests are failed by the transport as part of the close process.</p>
     */
    @Override
    public void close() {
        events.close();
        transport.close();
    }

    /**
     * Fluent builder for {@link McsmpClient}.
     *
     * <p>The builder mirrors {@link McsmpClientConfig.Builder} and adds convenience methods to either
     * {@link #build()} a disconnected client or {@link #connect()} immediately.</p>
     */
    public static final class Builder {

        private final McsmpClientConfig.Builder delegate = McsmpClientConfig.builder();

        private Builder() {
        }

        /**
         * Sets the WebSocket endpoint URI.
         *
         * @param endpoint a {@code ws://} or {@code wss://} management endpoint
         *
         * @return this builder
         */
        public Builder endpoint(URI endpoint) {
            delegate.endpoint(endpoint);
            return this;
        }

        /**
         * Sets the authentication strategy.
         *
         * @param auth the authentication strategy to apply during the WebSocket handshake
         *
         * @return this builder
         */
        public Builder auth(McsmpAuth auth) {
            delegate.auth(auth);
            return this;
        }

        /**
         * Configures bearer-token authentication with the server management secret.
         *
         * @param secret the non-blank management secret
         *
         * @return this builder
         */
        public Builder secret(String secret) {
            delegate.secret(secret);
            return this;
        }

        /**
         * Sets the optional HTTP {@code Origin} header.
         *
         * @param origin the origin value allowed by the server, or {@code null} to omit it
         *
         * @return this builder
         */
        public Builder origin(String origin) {
            delegate.origin(origin);
            return this;
        }

        /**
         * Sets the WebSocket opening-handshake timeout.
         *
         * @param connectTimeout a positive duration
         *
         * @return this builder
         */
        public Builder connectTimeout(java.time.Duration connectTimeout) {
            delegate.connectTimeout(connectTimeout);
            return this;
        }

        /**
         * Sets the timeout applied to each JSON-RPC request.
         *
         * @param requestTimeout a positive duration
         *
         * @return this builder
         */
        public Builder requestTimeout(java.time.Duration requestTimeout) {
            delegate.requestTimeout(requestTimeout);
            return this;
        }

        /**
         * Sets a custom SSL context for {@code wss://} connections.
         *
         * @param sslContext the SSL context to use, or {@code null} for the JDK default
         *
         * @return this builder
         */
        public Builder sslContext(javax.net.ssl.SSLContext sslContext) {
            delegate.sslContext(sslContext);
            return this;
        }

        /**
         * Adds or replaces a custom HTTP header for the opening handshake.
         *
         * @param name  the non-blank header name
         * @param value the header value
         *
         * @return this builder
         */
        public Builder header(String name, String value) {
            delegate.header(name, value);
            return this;
        }

        /**
         * Sets the Jackson mapper used by the client.
         *
         * @param objectMapper the mapper used for all request, response, and notification JSON mapping
         *
         * @return this builder
         */
        public Builder objectMapper(tools.jackson.databind.ObjectMapper objectMapper) {
            delegate.objectMapper(objectMapper);
            return this;
        }

        /**
         * Enables or disables local compatibility with the legacy notification prefix.
         *
         * @param legacyNotificationPrefix whether to normalize legacy notification method names
         *
         * @return this builder
         */
        public Builder legacyNotificationPrefix(boolean legacyNotificationPrefix) {
            delegate.legacyNotificationPrefix(legacyNotificationPrefix);
            return this;
        }

        /**
         * Sets the protocol version compatibility policy.
         *
         * @param versionPolicy the compatibility policy to use
         *
         * @return this builder
         */
        public Builder versionPolicy(McsmpVersionPolicy versionPolicy) {
            delegate.versionPolicy(versionPolicy);
            return this;
        }

        /**
         * Builds and connects a client asynchronously.
         *
         * @return a future that completes with the connected client
         */
        public CompletableFuture<McsmpClient> connect() {
            return build().connect();
        }

        /**
         * Builds a disconnected client.
         *
         * @return a new client using the current builder configuration
         */
        public McsmpClient build() {
            return McsmpClient.create(delegate.build());
        }

    }

}
