/**
 * Typed server notification events and listener registration APIs.
 *
 * <p>Official {@code minecraft:notification/*} messages are decoded into immutable records in this package.
 * Unknown official notifications and custom namespace notifications remain available through raw listeners as
 * {@link top.chiloven.mcsmp4j.event.RawMcsmpEvent}. Event listeners can be registered through
 * {@link top.chiloven.mcsmp4j.McsmpClient#events()}.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.event;

import org.jspecify.annotations.NullMarked;
