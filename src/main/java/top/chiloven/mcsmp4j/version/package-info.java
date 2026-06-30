/**
 * Protocol-version helper types and named feature gates.
 *
 * <p>This package does not hard-code Minecraft server behavior into the client. Instead, it names protocol-history
 * features so that applications can express their compatibility requirements clearly and combine those requirements
 * with runtime discovery from {@link top.chiloven.mcsmp4j.discovery.McsmpCapabilities}.</p>
 *
 * <h2>Version and feature separation</h2>
 *
 * <p>{@link top.chiloven.mcsmp4j.version.McsmpProtocolVersion} is a semantic version value reported or inferred from
 * server discovery. {@link top.chiloven.mcsmp4j.version.McsmpFeature} is a named capability such as typed game-rule
 * values or world-upgrade notifications. Prefer feature checks over raw version comparisons whenever possible, because
 * custom servers and future vanilla versions may expose features in combinations not represented by historical
 * snapshots.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.version;

import org.jspecify.annotations.NullMarked;
