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
 * MCSMP client entry point.
 *
 * <p>Exposes connection lifecycle, raw JSON-RPC calls, raw notifications, and the core strongly-typed
 * MCSMP APIs implemented by this library.</p>
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

    public static Builder builder() {
        return new Builder();
    }

    public static CompletableFuture<McsmpClient> connect(McsmpClientConfig config) {
        var client = create(config);
        return client.connect();
    }

    public static McsmpClient create(McsmpClientConfig config) {
        return new McsmpClient(config);
    }

    public CompletableFuture<McsmpClient> connect() {
        return transport.connect().thenApply(ignored -> this);
    }

    public boolean isConnected() {
        return transport.isConnected();
    }

    public McsmpClientConfig config() {
        return config;
    }

    public RawApi raw() {
        return raw;
    }

    public AllowlistApi allowlist() {
        return allowlist;
    }

    public PlayersApi players() {
        return players;
    }

    public ServerApi server() {
        return server;
    }

    public BansApi bans() {
        return bans;
    }

    public IpBansApi ipBans() {
        return ipBans;
    }

    public OperatorsApi operators() {
        return operators;
    }

    public ServerSettingsApi serverSettings() {
        return serverSettings;
    }

    public GamerulesApi gamerules() {
        return gamerules;
    }

    public McsmpEvents events() {
        return events;
    }

    public McsmpDiscovery discovery() {
        return discovery;
    }

    public CompletableFuture<McsmpCapabilities> discover() {
        return discovery.discover().thenApply(discovered -> {
            capabilities = discovered;
            return discovered;
        });
    }

    public Optional<McsmpCapabilities> capabilities() {
        return Optional.ofNullable(capabilities);
    }

    public JsonRpcSubscription onNotification(JsonRpcNotificationListener listener) {
        return transport.subscribe(listener);
    }

    public CompletableFuture<Void> closeAsync() {
        events.close();
        return transport.closeAsync();
    }

    @Override
    public void close() {
        events.close();
        transport.close();
    }

    public static final class Builder {

        private final McsmpClientConfig.Builder delegate = McsmpClientConfig.builder();

        private Builder() {
        }

        public Builder endpoint(URI endpoint) {
            delegate.endpoint(endpoint);
            return this;
        }

        public Builder auth(McsmpAuth auth) {
            delegate.auth(auth);
            return this;
        }

        public Builder secret(String secret) {
            delegate.secret(secret);
            return this;
        }

        public Builder origin(String origin) {
            delegate.origin(origin);
            return this;
        }

        public Builder connectTimeout(java.time.Duration connectTimeout) {
            delegate.connectTimeout(connectTimeout);
            return this;
        }

        public Builder requestTimeout(java.time.Duration requestTimeout) {
            delegate.requestTimeout(requestTimeout);
            return this;
        }

        public Builder sslContext(javax.net.ssl.SSLContext sslContext) {
            delegate.sslContext(sslContext);
            return this;
        }

        public Builder header(String name, String value) {
            delegate.header(name, value);
            return this;
        }

        public Builder objectMapper(tools.jackson.databind.ObjectMapper objectMapper) {
            delegate.objectMapper(objectMapper);
            return this;
        }

        public Builder legacyNotificationPrefix(boolean legacyNotificationPrefix) {
            delegate.legacyNotificationPrefix(legacyNotificationPrefix);
            return this;
        }

        public Builder versionPolicy(McsmpVersionPolicy versionPolicy) {
            delegate.versionPolicy(versionPolicy);
            return this;
        }

        public CompletableFuture<McsmpClient> connect() {
            return build().connect();
        }

        public McsmpClient build() {
            return McsmpClient.create(delegate.build());
        }

    }

}
