package com.backbase.android.res.deferred

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.Px
import dev.drewhamilton.extracare.DataApi
import kotlin.math.roundToInt

/**
 * A wrapper for resolving an integer dimension on demand.
 */
interface DeferredDimension {

    /**
     * Resolve the dimension as an integer pixel value for use as a size. The exact value is rounded, and non-zero exact
     * values are ensured to be at least one pixel in size.
     */
    @Px fun resolveAsSize(context: Context): Int

    /**
     * Resolve the dimension as an integer pixel value. The exact value is truncated to an integer.
     */
    @Px fun resolveAsOffset(context: Context): Int

    /**
     * Resolve the dimension's exact value.
     */
    @Px fun resolveExact(context: Context): Float

    /**
     * A wrapper for a constant integer [pxValue].
     */
    @DataApi class Constant(
        @Px private val pxValue: Float
    ) : DeferredDimension {

        /**
         * Convenience for initializing with an integer [pxValue].
         */
        constructor(pxValue: Int) : this(pxValue.toFloat())

        /**
         * Rounds [pxValue] to an integer. If [pxValue] is non-zero but rounds to zero, returns 1 pixel. [context] is
         * ignored.
         */
        @Px override fun resolveAsSize(context: Context): Int {
            val rounded = pxValue.roundToInt()
            return when {
                rounded != 0 -> rounded
                pxValue == 0f -> 0
                pxValue > 0f -> 1
                else -> -1
            }
        }

        /**
         * Truncates [pxValue] to an integer pixel value. [context] is ignored.
         */
        @Px override fun resolveAsOffset(context: Context): Int = pxValue.toInt()

        /**
         * Returns [pxValue]. [context] is ignored.
         */
        override fun resolveExact(context: Context): Float = pxValue
    }

    /**
     * A wrapper for a [DimenRes] [resId].
     */
    @DataApi class Resource(
        @DimenRes private val resId: Int
    ) : DeferredDimension {
        /**
         * Resolve [resId] to a pixel dimension size with the given [context].
         */
        @Px override fun resolveAsSize(context: Context): Int = context.resources.getDimensionPixelSize(resId)

        /**
         * Resolve [resId] to a pixel dimension offset with the given [context].
         */
        @Px override fun resolveAsOffset(context: Context): Int = context.resources.getDimensionPixelOffset(resId)

        /**
         * Resolve [resId] to a pixel dimension with the given [context].
         */
        @Px override fun resolveExact(context: Context): Float = context.resources.getDimension(resId)
    }
}
