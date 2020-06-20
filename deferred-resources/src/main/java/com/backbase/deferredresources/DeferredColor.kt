package com.backbase.deferredresources

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.backbase.deferredresources.internal.resolveAttribute
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a [ColorInt] color on demand.
 */
interface DeferredColor {

    /**
     * Resolve the [ColorInt] color.
     */
    @ColorInt fun resolve(context: Context): Int

    /**
     * A wrapper for a constant color [value].
     */
    @DataApi class Constant(
        @ColorInt private val value: Int
    ) : DeferredColor {

        /**
         * Convenience for wrapping a constant color value parsed from the given [colorString].
         */
        constructor(colorString: String) : this(Color.parseColor(colorString))

        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context) = value
    }

    /**
     * A wrapper for a [ColorRes] [resId].
     */
    @DataApi class Resource(
        @ColorRes private val resId: Int
    ) : DeferredColor {
        /**
         * Resolve [resId] to a [ColorInt] with the given [context].
         */
        override fun resolve(context: Context) = ContextCompat.getColor(context, resId)
    }

    /**
     * A wrapper for a [AttrRes] [resId] reference to a color.
     */
    @DataApi class Attribute(
        @AttrRes private val resId: Int
    ) : DeferredColor {

        // Re-used every time the color is resolved, for efficiency
        private val reusedTypedValue = TypedValue()

        /**
         * Resolve [resId] to a [ColorInt] with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a color.
         */
        override fun resolve(context: Context) = context.resolveColorAttribute(resId)

        @ColorInt
        private fun Context.resolveColorAttribute(@AttrRes resId: Int): Int =
            resolveAttribute(
                resId, "color", reusedTypedValue,
                TypedValue.TYPE_INT_COLOR_RGB8, TypedValue.TYPE_INT_COLOR_ARGB8,
                TypedValue.TYPE_INT_COLOR_RGB4, TypedValue.TYPE_INT_COLOR_ARGB4
            ) {
                data
            }
    }
}
