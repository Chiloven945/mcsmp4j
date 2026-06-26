/**
 * Public entry points for mcsmp4j.
 *
 * <p>This package contains the client, configuration, authentication strategies, low-level raw API, and exception
 * hierarchy. A typical application starts with {@link top.chiloven.mcsmp4j.McsmpClient#builder()}, connects to a
 * Minecraft dedicated server management endpoint, and then uses strongly typed API groups from
 * {@link top.chiloven.mcsmp4j.McsmpClient}.</p>
 *
 * <p>All network operations are asynchronous and return {@link java.util.concurrent.CompletableFuture}. The
 * library does not own an application-level scheduler; callers decide whether to block, compose futures, or bridge them
 * into their own executor and UI framework.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j;

import org.jspecify.annotations.NullMarked;
