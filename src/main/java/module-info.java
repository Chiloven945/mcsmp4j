/**
 * Java module for mcsmp4j, a Java 21 client library for the Minecraft Server Management Protocol.
 *
 * <p>The module exports the public client entry points, typed official APIs, immutable model records,
 * notification event types, discovery capability types, low-level protocol notification types, and protocol version
 * helpers. Internal transport, JSON-RPC, and implementation packages are intentionally not exported.</p>
 */
module top.chiloven.mcsmp4j {

    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires static org.jspecify;

    exports top.chiloven.mcsmp4j;
    exports top.chiloven.mcsmp4j.api;
    exports top.chiloven.mcsmp4j.model;
    exports top.chiloven.mcsmp4j.protocol;
    exports top.chiloven.mcsmp4j.event;
    exports top.chiloven.mcsmp4j.discovery;
    exports top.chiloven.mcsmp4j.version;

}
