package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.ArrayRes
import com.backbase.deferredresources.DeferredTextArray.Resource.Type
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a text array on demand.
 */
interface DeferredTextArray {

    /**
     * Resolve the text array.
     */
    fun resolve(context: Context): Array<out CharSequence>

    /**
     * A wrapper for a constant array of text [values].
     *
     * This class protect against mutability of by holding a copy of the input [values] and by always returning a new
     * copy of those [values] when resolved.
     */
    @DataApi class Constant private constructor(
        // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
        @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
        private val values: Array<out CharSequence>
    ) : DeferredTextArray {

        /**
         * Initialize with the given text [values].
         *
         * The given [values] array is copied on construction, so later external changes to the original will not be
         * reflected in this [DeferredTextArray].
         */
        constructor(vararg values: CharSequence) : this(1, arrayOf(*values))

        /**
         * Convenience for initializing with a [Collection] of text [values].
         */
        constructor(values: Collection<CharSequence>) : this(1, values.toTypedArray())

        /**
         * Always resolves to a new array copied from [values]. Changes to the returned array will not be reflected in
         * future calls to resolve this [DeferredTextArray].
         */
        override fun resolve(context: Context): Array<out CharSequence> = values.copyOf()

        /**
         * Two instances of [DeferredTextArray.Constant] are considered equals if they hold the same number of text
         * values and each text value in this instance is equal to the corresponding text value in [other].
         */
        override fun equals(other: Any?): Boolean = other is Constant && values.contentEquals(other.values)

        /**
         * Equal to the hash code of this instance's text values plus an offset.
         */
        override fun hashCode(): Int = 101 + values.contentHashCode()
    }

    /**
     * A wrapper for a text [ArrayRes] [id]. Optionally set [type] to [Type.TEXT] to retain style information in each
     * resource.
     */
    @DataApi class Resource @JvmOverloads constructor(
        @ArrayRes private val id: Int,
        private val type: Type = Type.STRING
    ) : DeferredTextArray {
        /**
         * Resolve [id] to a text array with the given [context].
         *
         * If [type] is [Type.STRING], resolves an array of (un-styled) strings. If [type] is [Type.TEXT], resolves an
         * array of styled [CharSequence]s.
         *
         * @see android.content.res.Resources.getStringArray
         * @see android.content.res.Resources.getTextArray
         */
        override fun resolve(context: Context): Array<out CharSequence> = when (type) {
            Type.STRING -> context.resources.getStringArray(id)
            Type.TEXT -> context.resources.getTextArray(id)
        }

        /**
         * The type of text resource to resolve.
         */
        enum class Type {
            STRING, TEXT
        }
    }


}
