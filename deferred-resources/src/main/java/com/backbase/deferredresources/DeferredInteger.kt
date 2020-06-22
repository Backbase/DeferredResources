package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.IntegerRes
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving an integer on demand.
 */
interface DeferredInteger {

    /**
     * Resolve the integer.
     */
    fun resolve(context: Context): Int

    /**
     * A wrapper for a constant integer [value].
     */
    @DataApi class Constant(
        private val value: Int
    ) : DeferredInteger {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Int = value
    }

    /**
     * A wrapper for a [IntegerRes] [resId].
     */
    @DataApi class Resource(
        @IntegerRes private val resId: Int
    ) : DeferredInteger {
        /**
         * Resolve [resId] to an integer with the given [context].
         */
        override fun resolve(context: Context): Int = context.resources.getInteger(resId)
    }
}
