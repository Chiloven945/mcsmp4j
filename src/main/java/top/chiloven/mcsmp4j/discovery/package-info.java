/**
 * Runtime discovery and capability modeling for MCSMP servers.
 *
 * <p>The discovery package wraps the JSON-RPC {@code rpc.discover} method and transforms the returned schema into a
 * compact capability model. This is the recommended way to decide whether a server supports a method, notification, or
 * feature that was introduced after the first public protocol versions.</p>
 *
 * <h2>Why discovery matters</h2>
 *
 * <p>The MCSMP surface has changed over time. Authentication, browser-friendly subprotocol authentication, status
 * activity
 * notifications, typed game-rule values, pre-start management availability, and world-upgrade notifications appeared in
 * different protocol revisions. Minecraft version strings are useful for display, but the runtime method and
 * notification sets are a more reliable source of truth for client behavior.</p>
 *
 * <h2>Typical use</h2>
 *
 * <pre>{@code
 * McsmpCapabilities caps = client.discover().join();
 * if (caps.supports(McsmpFeature.WORLD_UPGRADE_NOTIFICATIONS)) {
 *     client.events().on(WorldUpgradeProgressEvent.class, event -> showProgress(event.progress()));
 * }
 * }</pre>
 *
 * <p>Capabilities are immutable snapshots. Calling {@link top.chiloven.mcsmp4j.McsmpClient#discover()} again replaces
 * the
 * client's cached value but does not mutate a previously returned
 * {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities} instance.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.NullMarked;
