package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredPlurals] to a normal [DeferredText] by providing its [quantity] to be used when resolved.
 */
@JvmSynthetic public fun DeferredPlurals.withQuantity(quantity: Int): DeferredText =
    QuantifiedDeferredText(wrapped = this, quantity = quantity)

/**
 * A [DeferredText] implementation that wraps a [DeferredPlurals] along with its [quantity]. Designed for use cases
 * where the quantity is determined at the declaring site rather than the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredPlurals].
 */
@DataApi public class QuantifiedDeferredText(
    private val wrapped: DeferredPlurals,
    private val quantity: Int
) : DeferredText {
    /**
     * Resolve [wrapped] for [quantity] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = wrapped.resolve(context, quantity)
}
