/**
 * Type-safe wrappers for the official MCSMP method groups.
 *
 * <p>Interfaces in this package are grouped exactly like the official {@code minecraft:*} JSON-RPC namespace. They are
 * obtained from {@link top.chiloven.mcsmp4j.McsmpClient} and translate Java values into protocol parameters while
 * returning immutable model objects from {@link top.chiloven.mcsmp4j.model}.</p>
 *
 * <h2>Method naming</h2>
 *
 * <p>The Java names intentionally read like normal Java service calls, while the implementation preserves the protocol
 * method names. For example, {@link top.chiloven.mcsmp4j.api.AllowlistApi#add(java.util.Collection)} calls
 * {@code minecraft:allowlist/add}; {@link top.chiloven.mcsmp4j.api.ServerApi#status()} calls
 * {@code minecraft:server/status}; and
 * {@link top.chiloven.mcsmp4j.api.GamerulesApi#update(top.chiloven.mcsmp4j.model.UntypedGameRule)} calls
 * {@code minecraft:gamerules/update}. This mapping makes the typed API convenient without hiding the underlying
 * protocol, which is useful when comparing logs, server errors, or discovery output.</p>
 *
 * <h2>Futures and exceptions</h2>
 *
 * <p>Every method returns a {@link java.util.concurrent.CompletableFuture}. The future completes successfully with the
 * business result from the server, completes exceptionally with {@link top.chiloven.mcsmp4j.McsmpRemoteException} when
 * the server returns a JSON-RPC error object, and completes exceptionally with a transport exception when the WebSocket
 * is closed, times out, or cannot serialize/deserialize a message.</p>
 *
 * <h2>Collections and defensive copying</h2>
 *
 * <p>Input collection parameters are interpreted as a single JSON-RPC array parameter. Varargs overloads are provided
 * for
 * small convenience calls. Returned lists are deserialized snapshots of the server result; they are not live views and
 * do not update after later notifications. Applications that need live state should combine these APIs with event
 * listeners from {@link top.chiloven.mcsmp4j.event}.</p>
 *
 * <h2>Capability checks</h2>
 *
 * <p>The official protocol has evolved over multiple versions. Applications that target mixed server versions should
 * call
 * {@link top.chiloven.mcsmp4j.McsmpClient#discover()} and inspect
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities} before using features introduced after the earliest
 * supported server. The typed APIs do not automatically suppress calls to unsupported methods, because servers and
 * extensions may expose custom combinations of methods.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.api;

import org.jspecify.annotations.NullMarked;
