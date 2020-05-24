package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredFormattedString
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredFormattedString] by providing its [quantity] to be used when
 * resolved. Format arguments must still be provided when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withQuantity(quantity: Int): DeferredFormattedString =
    QuantifiedDeferredFormattedString(this, quantity)

/**
 * A [DeferredFormattedString] implementation that wraps a [DeferredFormattedPlurals] along with its [quantity].
 * Designed for use cases where the quantity is determined at the declaring site while the format args are determined at
 * the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredFormattedPlurals]. If the format args
 * are to be determined at the declaring site, see [QuantifiedFormattedDeferredText].
 */
@DataApi class QuantifiedDeferredFormattedString(
    private val deferredFormattedPlurals: DeferredFormattedPlurals,
    private val quantity: Int
) : DeferredFormattedString {
    /**
     * Resolve [deferredFormattedPlurals] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context, vararg formatArgs: Any): String =
        deferredFormattedPlurals.resolve(context, quantity, formatArgs)
}
