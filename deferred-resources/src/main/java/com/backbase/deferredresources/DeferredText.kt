package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.StringRes
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving text on demand.
 */
interface DeferredText {

    /**
     * Resolve the text.
     */
    fun resolve(context: Context): CharSequence

    /**
     * A wrapper for a constant text [value].
     */
    @DataApi class Constant(
        private val value: CharSequence
    ) : DeferredText {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): CharSequence = value
    }

    /**
     * A wrapper for a text [resId].
     */
    @DataApi class Resource @JvmOverloads constructor(
        @StringRes private val resId: Int,
        private val type: Type = Type.STRING
    ) : DeferredText {

        /**
         * Resolve [resId] to text with the given [context].
         *
         * If [type] is [Type.STRING], resolves a localized string. If [type] is [Type.TEXT], resolves a localized,
         * styled [CharSequence].
         *
         * @see android.content.Context.getString
         * @see android.content.Context.getText
         */
        override fun resolve(context: Context): CharSequence = when (type) {
            Type.STRING -> context.getString(resId)
            Type.TEXT -> context.getText(resId)
        }

        /**
         * The type of text resource to resolve.
         */
        enum class Type {
            STRING, TEXT
        }
    }

    /**
     * A wrapper for pluralized text with the quantity already known at instantiation.
     */
    @DataApi class Quantified(
        private val deferredPlurals: DeferredPlurals,
        private val quantity: Int
    ) : DeferredText {
        /**
         * Resolve [deferredPlurals] for [quantity] using the given [context].
         */
        override fun resolve(context: Context): CharSequence = deferredPlurals.resolve(context, quantity)
    }

    /**
     * A wrapper for formatted text with the format arguments already known at instantiation.
     */
    @DataApi class Formatted private constructor(
        // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
        @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
        private val deferredFormattedString: DeferredFormattedString,
        private val formatArgs: Array<out Any>
    ) : DeferredText {

        /**
         * Initialize with the given [deferredFormattedString] and [formatArgs].
         *
         * This constructor protects against array mutability by making a copy of [formatArgs].
         */
        constructor(
            deferredFormattedString: DeferredFormattedString,
            vararg formatArgs: Any
        ) : this(1, deferredFormattedString, arrayOf(*formatArgs))

        /**
         * Resolve [deferredFormattedString] with [formatArgs] using the given [context].
         */
        override fun resolve(context: Context): CharSequence = deferredFormattedString.resolve(context, *formatArgs)

        /**
         * Two instances of [DeferredText.Formatted] are considered equals if they hold equals
         * [deferredFormattedString]s, they hold the same number of [formatArgs], and each format arg in this instance
         * is equals to the corresponding format arg in [other].
         */
        override fun equals(other: Any?): Boolean = other is Formatted &&
                this.deferredFormattedString == other.deferredFormattedString &&
                this.formatArgs.contentEquals(other.formatArgs)

        /**
         * A hash of the formatted string and its arguments.
         */
        override fun hashCode(): Int = 31 * deferredFormattedString.hashCode() + formatArgs.contentHashCode()
    }

    /**
     * A wrapper for format-able pluralized text with the quantity and format arguments already known at instantiation.
     */
    @DataApi class QuantifiedAndFormatted private constructor(
        // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
        @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
        private val deferredFormattedPlurals: DeferredFormattedPlurals,
        private val quantity: Int,
        private val formatArgs: Array<out Any> = arrayOf(quantity)
    ) : DeferredText {

        /**
         * Initialize with the given [deferredFormattedPlurals], [quantity], and [formatArgs].
         *
         * This constructor protects against array mutability by making a copy of [formatArgs].
         */
        constructor(
            deferredFormattedPlurals: DeferredFormattedPlurals,
            quantity: Int,
            vararg formatArgs: Any = arrayOf(quantity)
        ) : this(1, deferredFormattedPlurals, quantity, arrayOf(*formatArgs))

        /**
         * Resolve [deferredFormattedPlurals] with [quantity] and [formatArgs] using the given [context].
         */
        override fun resolve(context: Context): CharSequence =
            deferredFormattedPlurals.resolve(context, quantity, *formatArgs)

        /**
         * Two instances of [DeferredText.QuantifiedAndFormatted] are considered equals if they hold equals
         * [deferredFormattedPlurals], they hold the same [quantity], they hold the same number of [formatArgs], and
         * each format arg in this instance is equal to the corresponding format arg in [other].
         */
        override fun equals(other: Any?): Boolean = other is QuantifiedAndFormatted &&
                this.deferredFormattedPlurals == other.deferredFormattedPlurals &&
                this.quantity == other.quantity &&
                this.formatArgs.contentEquals(other.formatArgs)

        /**
         * A hash of the formatted plurals, their quantity, and their arguments.
         */
        override fun hashCode(): Int =
            (31 * deferredFormattedPlurals.hashCode()) * 31 + quantity.hashCode() + formatArgs.contentHashCode()
    }
}
