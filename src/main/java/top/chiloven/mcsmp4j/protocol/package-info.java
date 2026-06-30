/**
 * Low-level JSON-RPC notification types.
 *
 * <p>Most applications should start with the typed event API in {@link top.chiloven.mcsmp4j.event}. This package
 * exists
 * for applications that need to inspect the raw JSON-RPC method and parameters exactly as the server sent them. It is
 * also useful for custom namespaces, modded servers, protocol experiments, diagnostics, and compatibility tools.</p>
 *
 * <h2>Scope</h2>
 *
 * <p>These types model notifications and remote errors visible to users of the library. Request/response bookkeeping,
 * WebSocket framing, and codec internals are intentionally not exported. The low-level
 * {@link top.chiloven.mcsmp4j.RawApi} uses these concepts internally while still returning typed Jackson results to
 * callers.</p>
 *
 * <h2>Threading</h2>
 *
 * <p>Raw notification listeners are invoked from the same receive path as typed events. They should be quick and should
 * not
 * block on long-running operations. For heavy work, enqueue the notification into an application executor or message
 * queue.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.protocol;

import org.jspecify.annotations.NullMarked;
