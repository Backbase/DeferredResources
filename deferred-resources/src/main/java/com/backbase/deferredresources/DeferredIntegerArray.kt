package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.ArrayRes
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving an integer array on demand.
 */
interface DeferredIntegerArray {

    /**
     * Resolve the integer array.
     */
    fun resolve(context: Context): IntArray

    /**
     * A wrapper for a constant array of integer [values].
     *
     * This class protects against array mutability by holding a copy of the input [values] and by always returning a
     * new copy of those [values] when resolved.
     */
    @DataApi class Constant private constructor(
        // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
        @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Char,
        private val values: IntArray
    ) : DeferredIntegerArray {

        /**
         * Initialize with the given integer [values].
         *
         * The given [values] array is copied on construction, so later external changes to the original will not be
         * reflected in this [DeferredIntegerArray].
         */
        constructor(vararg values: Int) : this('x', intArrayOf(*values))

        /**
         * Convenience for initializing with a [Collection] of integer [values].
         */
        constructor(values: Collection<Int>) : this('x', values.toIntArray())

        /**
         * Always resolves to a new array copied from [values]. Changes to the returned array will not be reflected in
         * future calls to resolve this [DeferredIntegerArray].
         */
        override fun resolve(context: Context): IntArray = values.copyOf()

        /**
         * Two instances of [DeferredIntegerArray.Constant] are considered equals if they hold the same number of
         * integer values and each integer value in this instance is equal to the corresponding integer value in
         * [other].
         */
        override fun equals(other: Any?): Boolean = other is Constant && values.contentEquals(other.values)

        /**
         * Equal to the hash code of this instance's integer values plus an offset.
         */
        override fun hashCode(): Int = 103 + values.contentHashCode()
    }

    /**
     * A wrapper for an integer [ArrayRes] [id].
     */
    @DataApi class Resource(
        @ArrayRes private val id: Int
    ) : DeferredIntegerArray {
        /**
         * Resolve [id] to an integer array with the given [context].
         */
        override fun resolve(context: Context): IntArray = context.resources.getIntArray(id)
    }
}
