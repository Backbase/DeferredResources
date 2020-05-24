package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredPlurals
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredPlurals] by providing its [formatArgs] to be used when resolved.
 * A quantity must still be provided when resolved.
 */
@JvmSynthetic fun DeferredFormattedPlurals.withFormatArgs(vararg formatArgs: Any): DeferredPlurals =
    FormattedDeferredPlurals(this, *formatArgs)

/**
 * A [DeferredPlurals] implementation that wraps a [DeferredFormattedPlurals] along with its [formatArgs]. Designed for
 * use cases where the format args are determined at the declaring site while the quantity is determined at the
 * resolving site.
 *
 * If the format args are to be determined at the resolving site, stick with [DeferredFormattedPlurals]. If the
 * quantity is to be determined at the declaring site, see [QuantifiedFormattedDeferredText].
 */
@DataApi class FormattedDeferredPlurals private constructor(
    // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
    @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
    private val deferredFormattedPlurals: DeferredFormattedPlurals,
    private val formatArgs: Array<out Any>
) : DeferredPlurals {

    /**
     * Initialize with the given [deferredFormattedPlurals] and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    constructor(
        deferredFormattedPlurals: DeferredFormattedPlurals,
        vararg formatArgs: Any
    ) : this(1, deferredFormattedPlurals, arrayOf(*formatArgs))

    /**
     * Resolve [deferredFormattedPlurals] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context, quantity: Int): CharSequence =
        deferredFormattedPlurals.resolve(context, quantity, *formatArgs)

    /**
     * Two instances of [FormattedDeferredPlurals] are considered equals if they hold equals [deferredFormattedPlurals],
     * they hold the same number of [formatArgs], and each format arg in this instance is equal to the corresponding
     * format arg in [other].
     */
    override fun equals(other: Any?): Boolean = other is FormattedDeferredPlurals &&
            this.deferredFormattedPlurals == other.deferredFormattedPlurals &&
            this.formatArgs.contentEquals(other.formatArgs)

    /**
     * A hash of the formatted plurals and their format arguments.
     */
    override fun hashCode(): Int = 31 * deferredFormattedPlurals.hashCode() + formatArgs.contentHashCode()
}
