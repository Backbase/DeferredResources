package com.backbase.deferredresources

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.Px
import com.backbase.deferredresources.internal.toSize
import dev.drewhamilton.extracare.DataApi

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
        constructor(@Px pxValue: Int) : this(pxValue.toFloat())

        /**
         * Rounds [pxValue] to an integer. If [pxValue] is non-zero but rounds to zero, returns 1 pixel. [context] is
         * ignored.
         */
        @Px override fun resolveAsSize(context: Context): Int = pxValue.toSize()

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

    /**
     * A wrapper for an [AttrRes] [resId] reference to a dimension.
     */
    @DataApi class Attribute(
        @AttrRes private val resId: Int
    ) : DeferredDimension {

        // Re-used every time the dimension is resolved, for efficiency
        private val resolvedValue = TypedValue()

        /**
         * Resolve [resId] to a [Px] int for use as a size with the given [context]'s theme. The exact value is rounded,
         * and non-zero exact values are ensured to be at least one pixel in size.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px
        override fun resolveAsSize(context: Context): Int = resolveExact(context).toSize()

        /**
         * Resolve [resId] to a [Px] int for use as an offset with the given [context]'s theme. The exact value is
         * truncated to an integer.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px
        override fun resolveAsOffset(context: Context): Int = resolveExact(context).toInt()

        /**
         * Resolve [resId] to an exact [Px] value for use as a size with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px
        override fun resolveExact(context: Context): Float = context.resolveDimensionAttribute(resId)

        @Px
        private fun Context.resolveDimensionAttribute(@AttrRes resId: Int): Float {
            try {
                val isResolved = theme.resolveAttribute(resId, resolvedValue, true)
                if (isResolved && resolvedValue.type == TypedValue.TYPE_DIMENSION) {
                    return resolvedValue.getDimension(resources.displayMetrics)
                } else {
                    val errorMessage = createErrorMessage(resources, isResolved)
                    throw IllegalArgumentException(errorMessage)
                }
            } finally {
                // Clear for re-use:
                resolvedValue.setTo(EMPTY_VALUE)
            }
        }

        private fun createErrorMessage(resources: Resources, isResolved: Boolean) = try {
            val name = resources.getResourceName(resId)
            val infix = if (isResolved) " to a dimension" else ""
            "Attribute <$name> could not be resolved$infix in the given Context's theme"
        } catch (notFoundException: Resources.NotFoundException) {
            "Attribute <$resId> could not be found"
        }

        private companion object {
            private val EMPTY_VALUE = TypedValue()
        }
    }
}
