/**
 * Strongly typed Java facades for the official Minecraft Server Management Protocol method groups.
 *
 * <p>Each interface in this package maps one official MCSMP namespace to Java methods that return
 * {@link java.util.concurrent.CompletableFuture CompletableFuture} results. The wrappers handle JSON-RPC method names
 * and parameter wrapping internally, so callers can work with immutable model records instead of raw JSON. For custom
 * server namespaces or protocol methods not yet wrapped here, use {@link top.chiloven.mcsmp4j.RawApi}.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.api;

import org.jspecify.annotations.NullMarked;
