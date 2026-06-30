/**
 * Immutable protocol model records, enums, and sealed value types.
 *
 * <p>The model package is the schema layer of mcsmp4j. Its types are designed to be safe application data objects
 * rather
 * than mutable transport buffers. Records validate mandatory fields in compact constructors, reject blank strings where
 * the protocol would not have a meaningful value, and defensively copy collections. As a result, the same instance may
 * be shared between callbacks, futures, logs, and application caches without accidental mutation.</p>
 *
 * <h2>Nullness contract</h2>
 *
 * <p>The package is {@link org.jspecify.annotations.NullMarked}. Parameters and return values are non-null unless a
 * component is explicitly annotated with {@link org.jspecify.annotations.Nullable}. Optional protocol fields such as
 * ban reasons, ban sources, expiration instants, player UUIDs, and player names use nullable components because the
 * server may legitimately omit them or request payloads may identify an object using only one of several
 * identifiers.</p>
 *
 * <h2>JSON mapping</h2>
 *
 * <p>Models are annotated for Jackson serialization using the protocol's field names and scalar values. Enums serialize
 * to
 * the lowercase strings expected by Minecraft. {@link top.chiloven.mcsmp4j.model.GameRuleValue} is intentionally a
 * sealed interface because protocol version {@code 2.0.0} changed game-rule values from legacy strings to typed JSON
 * booleans and integers. Keeping all shapes in one value type allows clients to interoperate with older experimental
 * servers, modern vanilla servers, and custom implementations.</p>
 *
 * <h2>Request models versus response models</h2>
 *
 * <p>Several records are used in both directions. {@link top.chiloven.mcsmp4j.model.Player}, for example, can be sent
 * as a
 * selector containing only a UUID or only a name, but responses commonly include both. Ban records can be sent as
 * desired state and returned as server state. Do not assume a response object was echoed unchanged from the request;
 * the server may normalize names, add UUIDs, apply default sources, or reject unknown identifiers with a remote
 * error.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.model;

import org.jspecify.annotations.NullMarked;
