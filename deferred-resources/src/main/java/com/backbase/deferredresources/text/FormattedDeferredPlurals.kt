package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredPlurals
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredPlurals] by providing [formatArgs] to be used when resolved.
 * A quantity must still be provided when resolved.
 */
@JvmSynthetic public fun DeferredFormattedPlurals.withFormatArgs(vararg formatArgs: Any): FormattedDeferredPlurals =
    FormattedDeferredPlurals(wrapped = this, formatArgs = formatArgs)

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredPlurals] by providing its [formatArgs] to be used when resolved.
 * A quantity must still be provided when resolved.
 */
@Suppress("unused")
@Deprecated("Covariant return type introduced", level = DeprecationLevel.HIDDEN)
// Unused generic is added to allow return-type overload
@JvmSynthetic public fun <T> DeferredFormattedPlurals.withFormatArgs(vararg formatArgs: Any): DeferredPlurals =
    FormattedDeferredPlurals(wrapped = this, formatArgs = formatArgs)

/**
 * A [DeferredPlurals] implementation that wraps a [DeferredFormattedPlurals] along with its [formatArgs]. Designed for
 * use cases where the format args are determined at the declaring site while the quantity is determined at the
 * resolving site.
 *
 * If the format args are to be determined at the resolving site, stick with [DeferredFormattedPlurals]. If the
 * quantity is to be determined at the declaring site, see [QuantifiedFormattedDeferredText].
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [wrapped] or any of [formatArgs] cannot be
 * marshalled.
 */
// Primary constructor is internal rather than private so the generated Creator can access it
@Parcelize
@DataApi public class FormattedDeferredPlurals internal constructor(
    private val wrapped: @RawValue DeferredFormattedPlurals,
    private val formatArgs: @RawValue List<Any>,
) : ParcelableDeferredPlurals {

    /**
     * Initialize with the given [wrapped] [DeferredFormattedPlurals] and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    public constructor(
        wrapped: DeferredFormattedPlurals,
        vararg formatArgs: Any
    ) : this(wrapped, formatArgs.toList())

    /**
     * Resolve [wrapped] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context, quantity: Int): CharSequence =
        wrapped.resolve(context, quantity, *formatArgs.toTypedArray())
}
