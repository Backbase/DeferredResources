package com.backbase.deferredresources

// TODO: Settle on package

/**
 * Convert a [DeferredFormattedString] to a normal [DeferredText] by providing its [formatArgs] to be used when
 * resolved.
 */
@JvmSynthetic fun DeferredFormattedString.withFormatArgs(vararg formatArgs: Any): DeferredText =
    DeferredText.Formatted(this, formatArgs)

/**
 * Convert a [DeferredPlurals] to a normal [DeferredText] by providing its [quantity] to be used when resolved.
 */
@JvmSynthetic fun DeferredPlurals.withQuantity(quantity: Int): DeferredText = DeferredText.Quantified(this, quantity)

/**
 * Convert a [DeferredFormattedPlurals] to a normal [DeferredText] by providing its [quantity] and [formatArgs] to be
 * used when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withQuantityAndFormatArgs(
    quantity: Int,
    vararg formatArgs: Any = arrayOf(quantity)
): DeferredText = DeferredText.QuantifiedAndFormatted(this, quantity, formatArgs)

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredPlurals] by providing its [formatArgs] to be used when resolved.
 * A quantity must still be provided when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withFormatArgs(vararg formatArgs: Any): DeferredPlurals =
    DeferredPlurals.Formatted(this, formatArgs)

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredFormattedString] by providing its [quantity] to be used when
 * resolved. Format arguments must still be provided when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withQuantity(quantity: Int): DeferredFormattedString =
    DeferredFormattedString.Quantified(this, quantity)
