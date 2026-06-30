/**
 * Root package of the mcsmp4j client library.
 *
 * <p>This package contains the objects that an application normally touches first:
 * {@link top.chiloven.mcsmp4j.McsmpClient},
 * {@link top.chiloven.mcsmp4j.McsmpClientConfig}, authentication strategies, exception types, and the low-level
 * {@link top.chiloven.mcsmp4j.RawApi}. The remaining exported packages build on these types:</p>
 *
 * <ul>
 *     <li>{@link top.chiloven.mcsmp4j.api} exposes type-safe wrappers for the official Minecraft namespace.</li>
 *     <li>{@link top.chiloven.mcsmp4j.model} contains immutable records and enums that mirror protocol schemas.</li>
 *     <li>{@link top.chiloven.mcsmp4j.event} turns JSON-RPC notifications into strongly typed Java events.</li>
 *     <li>{@link top.chiloven.mcsmp4j.discovery} describes runtime capabilities discovered through {@code rpc.discover}.</li>
 *     <li>{@link top.chiloven.mcsmp4j.version} names protocol-history features and compatibility policies.</li>
 *     <li>{@link top.chiloven.mcsmp4j.protocol} gives access to raw JSON-RPC notification data.</li>
 * </ul>
 *
 * <h2>Protocol model</h2>
 *
 * <p>Minecraft Server Management Protocol (MCSMP) is modeled as JSON-RPC 2.0 messages transported over a single
 * WebSocket connection. Every typed method in this library ultimately sends one JSON-RPC request whose method name looks
 * like {@code minecraft:server/status}, {@code minecraft:players/kick}, or {@code minecraft:gamerules/update}. Every
 * notification emitted by the server is received as a JSON-RPC notification and may be consumed either through typed
 * events or as raw protocol data.</p>
 *
 * <h2>Typical application flow</h2>
 *
 * <pre>{@code
 * try (McsmpClient client = McsmpClient.builder()
 *         .endpoint(URI.create("wss://localhost:25585"))
 *         .secret(System.getenv("MCSMP_SECRET"))
 *         .origin("mcsmp4j")
 *         .build()) {
 *     client.connect().join();
 *     client.discover().join();
 *     client.server().status().thenAccept(System.out::println).join();
 * }
 * }</pre>
 *
 * <h2>Threading and asynchronous completion</h2>
 *
 * <p>The public API is asynchronous. Methods return {@link java.util.concurrent.CompletableFuture} and complete when a
 * matching JSON-RPC response is received or when a timeout/transport failure occurs. The library does not require callers
 * to dedicate a thread to the WebSocket; callbacks are invoked by the underlying JDK WebSocket machinery. Application code
 * should avoid doing expensive work directly inside notification listeners. Use your own executor when listener work may
 * block, perform file I/O, contact databases, or call back into game-server operations.</p>
 *
 * <h2>Error handling</h2>
 *
 * <p>All library-specific failures extend {@link top.chiloven.mcsmp4j.McsmpException}. Connection and handshake failures
 * are reported as {@link top.chiloven.mcsmp4j.McsmpConnectionException} or
 * {@link top.chiloven.mcsmp4j.McsmpAuthenticationException}; malformed protocol data is reported as
 * {@link top.chiloven.mcsmp4j.McsmpProtocolException}; JSON-RPC error responses from the server are reported as
 * {@link top.chiloven.mcsmp4j.McsmpRemoteException}. Feature checks should use the discovery package instead of relying
 * only on Minecraft version strings.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j;

import org.jspecify.annotations.NullMarked;
