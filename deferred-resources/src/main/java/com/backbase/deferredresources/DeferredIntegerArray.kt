package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.ArrayRes
import com.backbase.deferredresources.integer.ParcelableDeferredIntegerArray
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving an integer array on demand.
 */
public interface DeferredIntegerArray {

    /**
     * Resolve the integer array.
     */
    public fun resolve(context: Context): IntArray

    /**
     * A wrapper for a constant array of integer [values].
     *
     * This class protects against array mutability by holding a copy of the input [values] and by always returning a
     * new copy of those [values] when resolved.
     */
    // Primary constructor is internal rather than private so the generated Creator can access it
    @Parcelize
    @DataApi public class Constant internal constructor(
        private val values: List<Int>
    ) : ParcelableDeferredIntegerArray {

        /**
         * Initialize with the given integer [values].
         *
         * The given [values] array is copied on construction, so later external changes to the original will not be
         * reflected in this [DeferredIntegerArray].
         */
        public constructor(vararg values: Int) : this(values.toList())

        /**
         * Convenience for initializing with a [Collection] of integer [values].
         */
        public constructor(values: Collection<Int>) : this(values.toList())

        /**
         * Always resolves to a new array copied from [values]. Changes to the returned array will not be reflected in
         * future calls to resolve this [DeferredIntegerArray].
         */
        override fun resolve(context: Context): IntArray = values.toIntArray()
    }

    /**
     * A wrapper for an integer [ArrayRes] [id].
     */
    @Parcelize
    @DataApi public class Resource(
        @ArrayRes private val id: Int
    ) : ParcelableDeferredIntegerArray {
        /**
         * Resolve [id] to an integer array with the given [context].
         */
        override fun resolve(context: Context): IntArray = context.resources.getIntArray(id)
    }
}
