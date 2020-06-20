package com.backbase.deferredresources

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import com.backbase.deferredresources.internal.resolveAttribute
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a boolean on demand.
 */
interface DeferredBoolean {

    /**
     * Resolve the boolean.
     */
    fun resolve(context: Context): Boolean

    /**
     * A wrapper for a constant boolean [value].
     */
    @DataApi class Constant(
        private val value: Boolean
    ) : DeferredBoolean {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context) = value
    }

    /**
     * A wrapper for a [BoolRes] [resId].
     */
    @DataApi class Resource(
        @BoolRes private val resId: Int
    ) : DeferredBoolean {
        /**
         * Resolve [resId] to a boolean with the given [context].
         */
        override fun resolve(context: Context) = context.resources.getBoolean(resId)
    }

    @DataApi class Attribute(
        @AttrRes private val resId: Int
    ) : DeferredBoolean {

        // Re-used every time the boolean is resolved, for efficiency
        private val reusedTypedValue = TypedValue()

        /**
         * Resolve [resId] to a boolean with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a boolean.
         */
        override fun resolve(context: Context) = context.resolveBooleanAttribute(resId)

        private fun Context.resolveBooleanAttribute(@AttrRes resId: Int): Boolean =
            resolveAttribute(resId, "boolean", reusedTypedValue, TypedValue.TYPE_INT_BOOLEAN) {
                data != 0
            }
    }
}
