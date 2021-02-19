package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredPlurals] to a normal [DeferredText] by providing a [quantity] to be used when resolved.
 */
@JvmSynthetic public fun DeferredPlurals.withQuantity(quantity: Int): QuantifiedDeferredText =
    QuantifiedDeferredText(wrapped = this, quantity = quantity)

/**
 * Convert a [DeferredPlurals] to a normal [DeferredText] by providing a [quantity] to be used when resolved.
 */
@Suppress("unused")
@Deprecated("Covariant return type introduced", level = DeprecationLevel.HIDDEN)
// Unused generic is added to allow return-type overload
@JvmSynthetic public fun <T> DeferredPlurals.withQuantity(quantity: Int): DeferredText =
    QuantifiedDeferredText(wrapped = this, quantity = quantity)

/**
 * A [DeferredText] implementation that wraps a [DeferredPlurals] along with its [quantity]. Designed for use cases
 * where the quantity is determined at the declaring site rather than the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredPlurals].
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [wrapped] cannot be marshalled.
 */
@Parcelize
@Poko public class QuantifiedDeferredText(
    private val wrapped: @RawValue DeferredPlurals,
    private val quantity: Int
) : ParcelableDeferredText {
    /**
     * Resolve [wrapped] for [quantity] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = wrapped.resolve(context, quantity)
}
