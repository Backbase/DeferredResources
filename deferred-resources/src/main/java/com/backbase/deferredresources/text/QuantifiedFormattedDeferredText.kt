package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredFormattedPlurals] to a normal [DeferredText] by providing a [quantity] and [formatArgs] to be used
 * when resolved.
 */
@JvmSynthetic public fun DeferredFormattedPlurals.withQuantityAndFormatArgs(
    quantity: Int,
    vararg formatArgs: Any = arrayOf(quantity)
): DeferredText = QuantifiedFormattedDeferredText(wrapped = this, quantity = quantity, formatArgs = formatArgs)

/**
 * A [DeferredText] implementation that wraps a [DeferredFormattedPlurals] along with its [quantity] and [formatArgs].
 * Designed for use cases where the quantity and format args are determined at the declaring site rather than the
 * resolving site.
 *
 * If the quantity and format args are to be determined at the resolving site, stick with [DeferredFormattedPlurals].
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [wrapped] or any of [formatArgs] cannot be
 * marshalled.
 */
// Primary constructor is internal rather than private so the generated Creator can access it
@Parcelize
@DataApi public class QuantifiedFormattedDeferredText internal constructor(
    private val wrapped: @RawValue DeferredFormattedPlurals,
    private val quantity: Int,
    private val formatArgs: @RawValue List<Any>
) : ParcelableDeferredText {

    /**
     * Initialize with the given [wrapped] [DeferredFormattedPlurals], [quantity], and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    public constructor(
        wrapped: DeferredFormattedPlurals,
        quantity: Int,
        vararg formatArgs: Any = arrayOf(quantity)
    ) : this(wrapped, quantity, formatArgs.toList())

    /**
     * Resolve [wrapped] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context): CharSequence =
        wrapped.resolve(context, quantity, *formatArgs.toTypedArray())
}
