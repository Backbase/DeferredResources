package com.backbase.android.res.deferred

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a boolean on demand.
 */
sealed class DeferredBoolean {

    /**
     * Resolve the boolean.
     */
    abstract fun resolve(context: Context): Boolean

    /**
     * A wrapper for a constant boolean [value].
     */
    @DataApi class Constant(
        private val value: Boolean
    ) : DeferredBoolean() {
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
    ) : DeferredBoolean() {
        /**
         * Resolve [resId] to a boolean with the given [context].
         */
        override fun resolve(context: Context) = context.resources.getBoolean(resId)
    }

    @DataApi class Attribute(
        @AttrRes private val resId: Int
    ) : DeferredBoolean() {

        // Re-used every time the boolean is resolved, for efficiency
        private val resolvedValue = TypedValue()

        /**
         * Resolve [resId] to a boolean with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a boolean.
         */
        override fun resolve(context: Context) = context.theme.resolveBooleanAttribute(resId)

        private fun Resources.Theme.resolveBooleanAttribute(@AttrRes resId: Int): Boolean {
            val isResolved = resolveAttribute(resId, resolvedValue, true)
            val type = resolvedValue.type
            val data = resolvedValue.data
            // Clear for re-use:
            resolvedValue.setTo(EMPTY_VALUE)

            if (isResolved && type == TypedValue.TYPE_INT_BOOLEAN)
                return data != 0
            else
                throw IllegalArgumentException("Attribute <$resId> could not be resolved to a boolean")
        }

        private companion object {
            private val EMPTY_VALUE = TypedValue()
        }
    }
}
