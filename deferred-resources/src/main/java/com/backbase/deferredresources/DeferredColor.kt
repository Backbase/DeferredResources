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
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import com.backbase.deferredresources.color.ParcelableDeferredColor
import com.backbase.deferredresources.internal.resolveAttribute
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving a [ColorInt] color on demand.
 */
public interface DeferredColor {

    /**
     * Resolve the [ColorInt] color.
     */
    @ColorInt public fun resolve(context: Context): Int

    /**
     * Resolve the color to a [ColorStateList].
     */
    public fun resolveToStateList(context: Context): ColorStateList

    /**
     * A wrapper for a constant color [value].
     */
    @Parcelize
    @Poko public class Constant(
        @ColorInt private val value: Int
    ) : ParcelableDeferredColor {

        /**
         * Convenience for wrapping a constant color value parsed from the given [colorString].
         */
        public constructor(colorString: String) : this(Color.parseColor(colorString))

        /**
         * Always resolves to [value], ignoring [context].
         */
        @ColorInt override fun resolve(context: Context): Int = value

        /**
         * Always resolves to [value] wrapped in a new [ColorStateList].
         */
        override fun resolveToStateList(context: Context): ColorStateList = ColorStateList.valueOf(value)
    }

    /**
     * A wrapper for a [ColorRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @ColorRes private val resId: Int
    ) : ParcelableDeferredColor {
        /**
         * Resolve [resId] to a [ColorInt] with the given [context]. If [resId] resolves to a color selector resource,
         * resolves the default color of that selector.
         */
        @ColorInt override fun resolve(context: Context): Int =
            // Forward to `resolveToStateList` to ensure attribute-backed selectors resolve correctly:
            resolveToStateList(context).defaultColor

        /**
         * Resolve [resId] to a [ColorStateList] with the given [context].
         */
        override fun resolveToStateList(context: Context): ColorStateList =
            requireNotNull(AppCompatResources.getColorStateList(context, resId)) {
                "Could not resolve ${context.resources.getResourceName(resId)} to a ColorStateList with $context."
            }
    }

    /**
     * A wrapper for a [AttrRes] [resId] reference to a color.
     */
    @Parcelize
    @Poko public class Attribute(
        @AttrRes private val resId: Int
    ) : ParcelableDeferredColor {

        // Re-used every time the color is resolved, for efficiency
        @IgnoredOnParcel
        private val reusedTypedValue = TypedValue()

        /**
         * Resolve [resId] to a [ColorInt] with the given [context]'s theme. If [resId] would resolve a color selector,
         * resolves to the default color of that selector.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a color.
         */
        @ColorInt override fun resolve(context: Context): Int = context.resolveColorAttribute {
            if (type == TypedValue.TYPE_STRING)
                context.resolveColorStateList().defaultColor
            else
                data
        }

        override fun resolveToStateList(context: Context): ColorStateList = context.resolveColorAttribute {
            if (type == TypedValue.TYPE_STRING)
                context.resolveColorStateList()
            else
                ColorStateList.valueOf(data)
        }

        private inline fun <T> Context.resolveColorAttribute(
            toTypeSafeResult: TypedValue.() -> T
        ): T = resolveAttribute(
            resId, "color", reusedTypedValue,
            TypedValue.TYPE_INT_COLOR_RGB8, TypedValue.TYPE_INT_COLOR_ARGB8,
            TypedValue.TYPE_INT_COLOR_RGB4, TypedValue.TYPE_INT_COLOR_ARGB4,
            TypedValue.TYPE_STRING,
            toTypeSafeResult = toTypeSafeResult
        )

        private fun Context.resolveColorStateList(): ColorStateList = resolveAttribute(
            resId, "reference", reusedTypedValue,
            TypedValue.TYPE_REFERENCE,
            resolveRefs = false
        ) {
            val colorSelectorResId = data
            AppCompatResources.getColorStateList(this@resolveColorStateList, colorSelectorResId)
        }
    }

    /**
     * Companion object for [DeferredColor], public to allow extension factory functions in companion libraries.
     */
    public companion object
}
