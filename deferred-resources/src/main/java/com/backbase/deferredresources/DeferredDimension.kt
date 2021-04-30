/*
 * Copyright 2020 Backbase R&D B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.backbase.deferredresources

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Px
import com.backbase.deferredresources.DeferredDimension.Constant.Unit
import com.backbase.deferredresources.dimension.ParcelableDeferredDimension
import com.backbase.deferredresources.internal.resolveAttribute
import com.backbase.deferredresources.internal.toSize
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving an integer dimension on demand.
 */
public interface DeferredDimension {

    /**
     * Resolve the dimension as an integer pixel value for use as a size. The exact value is rounded, and non-zero exact
     * values are ensured to be at least one pixel in size.
     */
    @Px public fun resolveAsSize(context: Context): Int

    /**
     * Resolve the dimension as an integer pixel value. The exact value is truncated to an integer.
     */
    @Px public fun resolveAsOffset(context: Context): Int

    /**
     * Resolve the dimension's exact value.
     */
    @Px public fun resolveExact(context: Context): Float

    /**
     * A wrapper for a constant dimension [value]. If the given [unit] is [Unit.DP] or [Unit.SP], the resolved pixel
     * value will depend on the [Context] used to resolve it.
     */
    @Parcelize
    @Poko public class Constant(
        @Dimension private val value: Float,
        private val unit: Unit,
    ) : ParcelableDeferredDimension {

        /**
         * Convenience for initializing with an integer [value] of the given [unit].
         */
        public constructor(@Dimension value: Int, unit: Unit) : this(value.toFloat(), unit)

        /**
         * Convenience for initializing with a [pxValue] of [Unit.PX].
         */
        public constructor(@Px pxValue: Float) : this(pxValue, Unit.PX)

        /**
         * Convenience for initializing with an integer [pxValue] of [Unit.PX].
         */
        public constructor(@Px pxValue: Int) : this(pxValue.toFloat())

        /**
         * Rounds the resolved pixel value to an integer. If the pixel value is non-zero but rounds to zero, returns 1
         * pixel. [context] is used to convert the original DP or SP value to pixels.
         */
        @Px override fun resolveAsSize(context: Context): Int = pxValue(context).toSize()

        /**
         * Truncates the resolved pixel value to an integer. [context] is used to convert the original DP or SP value to
         * pixels.
         */
        @Px override fun resolveAsOffset(context: Context): Int = pxValue(context).toInt()

        /**
         * Returns the exact resolved pixel value. [context] is used to convert the original DP or SP value to pixels.
         */
        @Px override fun resolveExact(context: Context): Float = pxValue(context)

        /**
         * Convert [value] to a pixel value based on the [unit] and the given [context].
         */
        private fun pxValue(context: Context) = value * unit.scale(context)

        /**
         * Get the scale by which a value of a specific [Unit] should be multiplied to produce a pixel value.
         */
        private fun Unit.scale(context: Context): Float = when (this) {
            Unit.PX -> 1f
            Unit.DP -> context.resources.displayMetrics.density
            Unit.SP -> context.resources.displayMetrics.scaledDensity
        }

        /**
         * The measurement unit of the constant dimension value.
         */
        public enum class Unit {
            PX, DP, SP;
        }
    }

    /**
     * A wrapper for a [DimenRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @DimenRes private val resId: Int
    ) : ParcelableDeferredDimension {
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
    @Parcelize
    @Poko public class Attribute(
        @AttrRes private val resId: Int
    ) : ParcelableDeferredDimension {

        // Re-used every time the dimension is resolved, for efficiency
        @IgnoredOnParcel
        private val reusedTypedValue = TypedValue()

        /**
         * Resolve [resId] to a [Px] int for use as a size with the given [context]'s theme. The exact value is rounded,
         * and non-zero exact values are ensured to be at least one pixel in size.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px override fun resolveAsSize(context: Context): Int = resolveExact(context).toSize()

        /**
         * Resolve [resId] to a [Px] int for use as an offset with the given [context]'s theme. The exact value is
         * truncated to an integer.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px override fun resolveAsOffset(context: Context): Int = resolveExact(context).toInt()

        /**
         * Resolve [resId] to an exact [Px] value for use as a size with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a dimension.
         */
        @Px override fun resolveExact(context: Context): Float = context.resolveDimensionAttribute(resId)

        @Px private fun Context.resolveDimensionAttribute(@AttrRes resId: Int): Float =
            resolveAttribute(resId, "dimension", reusedTypedValue, TypedValue.TYPE_DIMENSION) {
                getDimension(resources.displayMetrics)
            }
    }
}
