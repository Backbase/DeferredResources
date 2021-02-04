package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.IntegerRes
import com.backbase.deferredresources.integer.ParcelableDeferredInteger
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving an integer on demand.
 */
public interface DeferredInteger {

    /**
     * Resolve the integer.
     */
    public fun resolve(context: Context): Int

    /**
     * A wrapper for a constant integer [value].
     */
    @Parcelize
    @DataApi public class Constant(
        private val value: Int
    ) : ParcelableDeferredInteger {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Int = value
    }

    /**
     * A wrapper for a [IntegerRes] [resId].
     */
    @Parcelize
    @DataApi public class Resource(
        @IntegerRes private val resId: Int
    ) : ParcelableDeferredInteger {
        /**
         * Resolve [resId] to an integer with the given [context].
         */
        override fun resolve(context: Context): Int = context.resources.getInteger(resId)
    }
}
