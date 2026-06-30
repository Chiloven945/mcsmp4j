/**
 * Typed event API for MCSMP JSON-RPC notifications.
 *
 * <p>MCSMP servers push notifications over the same WebSocket connection that carries request/response traffic. This
 * package gives those notifications Java event classes such as {@link top.chiloven.mcsmp4j.event.PlayerJoinedEvent},
 * {@link top.chiloven.mcsmp4j.event.ServerStatusEvent}, and {@link top.chiloven.mcsmp4j.event.GameRuleUpdatedEvent}.
 * Applications obtain the registry from {@link top.chiloven.mcsmp4j.McsmpClient#events()}.</p>
 *
 * <h2>Typed and raw consumption</h2>
 *
 * <p>Use {@link top.chiloven.mcsmp4j.event.McsmpEvents#on(Class, java.util.function.Consumer)} when you want a stable
 * Java type. Use {@link top.chiloven.mcsmp4j.event.McsmpEvents#onRaw(java.util.function.Consumer)} when you are
 * building a logging bridge, supporting custom namespaces, or experimenting with a newly added notification that
 * mcsmp4j does not yet model as a typed event.</p>
 *
 * <h2>Ordering and state</h2>
 *
 * <p>Events are delivered in the order in which the WebSocket listener receives complete text messages. The library
 * does
 * not persist event history. A listener registered after a player joins will not receive a synthetic join event; read a
 * snapshot with the corresponding API method, then subscribe if your application needs to maintain live state.</p>
 *
 * <h2>Listener lifecycle</h2>
 *
 * <p>Every registration returns an {@link top.chiloven.mcsmp4j.event.EventSubscription}. Close the subscription when
 * the
 * listener no longer belongs to an active component. Closing the {@link top.chiloven.mcsmp4j.McsmpClient} also closes
 * the event registry and removes registered listeners.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.event;

import org.jspecify.annotations.NullMarked;
