package com.backbase.deferredresources

import android.content.Context
import android.content.res.ColorStateList
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
     * Resolve the [ColorInt] color. If the underlying color is represented by a [ColorStateList], returns the default
     * color from that list.
     */
    @ColorInt fun resolve(context: Context): Int

    /**
     * Resolve the color to a [ColorStateList].
     */
    fun resolveToStateList(context: Context): ColorStateList

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
        @ColorInt override fun resolve(context: Context): Int = value

        /**
         * Always resolves to [value] wrapped in a new [ColorStateList].
         */
        override fun resolveToStateList(context: Context): ColorStateList =  ColorStateList.valueOf(value)
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
        @ColorInt override fun resolve(context: Context): Int = ContextCompat.getColor(context, resId)

        /**
         * Resolve [resId] to a [ColorStateList] with the given [context].
         */
        override fun resolveToStateList(context: Context): ColorStateList =
            requireNotNull(ContextCompat.getColorStateList(context, resId)) {
                "Could not resolve ${context.resources.getResourceName(resId)} to a ColorStateList with $context."
            }
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
        @ColorInt override fun resolve(context: Context): Int = context.resolveColorAttribute(resId)

        override fun resolveToStateList(context: Context): ColorStateList =
            context.resolveAttribute(resId, "color list", reusedTypedValue, TypedValue.TYPE_STRING) {
                // TODO:
                return ColorStateList.valueOf(resolve(context))
            }

        @ColorInt private fun Context.resolveColorAttribute(@AttrRes resId: Int): Int =
            resolveAttribute(
                resId, "color", reusedTypedValue,
                TypedValue.TYPE_INT_COLOR_RGB8, TypedValue.TYPE_INT_COLOR_ARGB8,
                TypedValue.TYPE_INT_COLOR_RGB4, TypedValue.TYPE_INT_COLOR_ARGB4
            ) {
                data
            }
    }
}
