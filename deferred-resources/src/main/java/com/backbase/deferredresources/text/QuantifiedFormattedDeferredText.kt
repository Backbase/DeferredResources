package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredFormattedPlurals] to a normal [DeferredText] by providing a [quantity] and [formatArgs] to be used
 * when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withQuantityAndFormatArgs(
    quantity: Int,
    vararg formatArgs: Any = arrayOf(quantity)
): DeferredText = QuantifiedFormattedDeferredText(wrapped = this, quantity = quantity, formatArgs = formatArgs)

/**
 * A [DeferredText] implementation that wraps a [DeferredFormattedPlurals] along with its [quantity] and [formatArgs].
 * Designed for use cases where the quantity and format args are determined at the declaring site rather than the
 * resolving site.
 *
 * If the quantity and format args are to be determined at the resolving site, stick with [DeferredFormattedPlurals].
 */
@DataApi class QuantifiedFormattedDeferredText private constructor(
    // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
    @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
    private val wrapped: DeferredFormattedPlurals,
    private val quantity: Int,
    private val formatArgs: Array<out Any> = arrayOf(quantity)
) : DeferredText {

    /**
     * Initialize with the given [wrapped] [DeferredFormattedPlurals], [quantity], and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    constructor(
        wrapped: DeferredFormattedPlurals,
        quantity: Int,
        vararg formatArgs: Any = arrayOf(quantity)
    ) : this(1, wrapped, quantity, arrayOf(*formatArgs))

    /**
     * Resolve [wrapped] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = wrapped.resolve(context, quantity, *formatArgs)

    /**
     * Two instances of [QuantifiedFormattedDeferredText] are considered equals if they wrap equals
     * [DeferredFormattedPlurals], they hold the same [quantity], they hold the same number of [formatArgs], and
     * each format arg in this instance is equal to the corresponding format arg in [other].
     */
    override fun equals(other: Any?): Boolean = other is QuantifiedFormattedDeferredText &&
            this.wrapped == other.wrapped &&
            this.quantity == other.quantity &&
            this.formatArgs.contentEquals(other.formatArgs)

    /**
     * A hash of the formatted plurals, their quantity, and their arguments.
     */
    override fun hashCode(): Int =
        (31 * wrapped.hashCode()) * 31 + quantity.hashCode() + formatArgs.contentHashCode()
}
