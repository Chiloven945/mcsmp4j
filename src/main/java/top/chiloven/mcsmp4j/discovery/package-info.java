/**
 * Runtime discovery and capability summaries based on {@code rpc.discover}.
 *
 * <p>The MCSMP protocol evolves across Minecraft versions. Types in this package let applications inspect the
 * methods, notifications, protocol version, and feature flags advertised by the connected server instead of relying
 * only on hard-coded version checks.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.discovery;

import org.jspecify.annotations.NullMarked;
