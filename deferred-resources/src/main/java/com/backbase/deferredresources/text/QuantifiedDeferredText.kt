package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredPlurals] to a normal [DeferredText] by providing its [quantity] to be used when resolved.
 */
@JvmSynthetic fun DeferredPlurals.withQuantity(quantity: Int): DeferredText = QuantifiedDeferredText(this, quantity)

/**
 * A [DeferredText] implementation that wraps a [DeferredPlurals] along with its [quantity]. Designed for use cases
 * where the quantity is determined at the declaring site rather than the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredPlurals].
 */
@DataApi class QuantifiedDeferredText(
    private val deferredPlurals: DeferredPlurals,
    private val quantity: Int
) : DeferredText {
    /**
     * Resolve [deferredPlurals] for [quantity] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = deferredPlurals.resolve(context, quantity)
}
