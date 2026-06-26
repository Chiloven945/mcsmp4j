/**
 * Immutable protocol model records and enums used by typed APIs and notification events.
 *
 * <p>The model layer mirrors MCSMP JSON schemas while using Java 21 records, enums, sealed interfaces, and
 * JSpecify nullness annotations. Constructors validate required fields and copy collections defensively, making model
 * instances safe to share between application components.</p>
 *
 * <p>Several models intentionally retain compatibility shapes from protocol history. For example,
 * {@link top.chiloven.mcsmp4j.model.GameRuleValue} supports legacy string values in addition to modern boolean and
 * integer values.</p>
 */
@NullMarked
package top.chiloven.mcsmp4j.model;

import org.jspecify.annotations.NullMarked;
