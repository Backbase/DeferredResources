package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredFormattedString
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredFormattedString] by providing its [quantity] to be used when
 * resolved. Format arguments must still be provided when resolved.
 */
@JvmSynthetic public fun DeferredFormattedPlurals.withQuantity(quantity: Int): DeferredFormattedString =
    QuantifiedDeferredFormattedString(wrapped = this, quantity = quantity)

/**
 * A [DeferredFormattedString] implementation that wraps a [DeferredFormattedPlurals] along with its [quantity].
 * Designed for use cases where the quantity is determined at the declaring site while the format args are determined at
 * the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredFormattedPlurals]. If the format args
 * are to be determined at the declaring site, see [QuantifiedFormattedDeferredText].
 *
 * This class implements [android.os.Parcelable], but will crash at runtime if it is parceled and [wrapped] does not
 * implement [android.os.Parcelable].
 */
@Parcelize
@DataApi public class QuantifiedDeferredFormattedString(
    private val wrapped: @RawValue DeferredFormattedPlurals,
    private val quantity: Int
) : ParcelableDeferredFormattedString {
    /**
     * Resolve [wrapped] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context, vararg formatArgs: Any): String =
        wrapped.resolve(context, quantity, *formatArgs)
}
