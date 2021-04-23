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

package com.backbase.deferredresources.demo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredText

class DemoPagerAdapter : RecyclerView.Adapter<DemoPagerAdapter.DeferredResourceViewHolder>() {

    private val formattedPluralsResource = DeferredFormattedPlurals.Resource(R.plurals.horses)

    fun getPageName(position: Int): CharSequence = when (position) {
        0 -> "Colors"
        1 -> "Plurals resource"
        2 -> "Drawables"
        else -> throw IllegalArgumentException("Position $position in adapter with size $itemCount")
    }

    override fun getItemCount(): Int = ViewType.values().size

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> ViewType.COLORS.ordinal
        1 -> ViewType.PLURALS.ordinal
        2 -> ViewType.DRAWABLES.ordinal
        else -> throw IndexOutOfBoundsException("Position $position in adapter with size $itemCount")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeferredResourceViewHolder {
        val view = when (ViewType.values()[viewType]) {
            ViewType.COLORS -> DeferredColorsView(parent.context)
            ViewType.PLURALS -> DeferredPluralsView(parent.context)
            ViewType.DRAWABLES -> DeferredDrawablesView(parent.context)
        }.apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return DeferredResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeferredResourceViewHolder, position: Int) {
        when (val view = holder.root) {
            is DeferredColorsView -> {
                view.display(
                    DeferredColor.Attribute(R.attr.colorSecondary),
                    DeferredText.Constant("Secondary")
                )
                view.display(
                    DeferredColor.Resource(R.color.backbase_red),
                    DeferredText.Constant("Backbase red")
                )
                view.display(
                    DeferredColor.Constant(Color.WHITE),
                    DeferredText.Constant("White")
                )
            }
            is DeferredPluralsView -> when (position) {
                1 -> view.display(formattedPluralsResource)
            }
            is DeferredDrawablesView -> {
                val context = holder.root.context
                view.display(
                    DeferredDrawable.Constant(
                        Circle(DeferredColor.Attribute(R.attr.colorPrimary).resolve(context))
                    )
                )
                view.display(
                    DeferredDrawable.Resource(R.drawable.ic_flower_24) {
                        setTint(DeferredColor.Resource(R.color.green), context)
                    }
                )
                view.display(
                    DeferredDrawable.Attribute(R.attr.demoIcon) {
                        setTint(DeferredColor.Attribute(R.attr.colorSecondary), context)
                    }
                )
            }
        }
    }

    private fun Drawable.setTint(color: DeferredColor, context: Context) {
        DrawableCompat.setTint(this, color.resolve(context))
    }

    @Suppress("FunctionName") // Factory
    private fun Circle(
        @ColorInt color: Int,
    ): Drawable = GradientDrawable(
        GradientDrawable.Orientation.BOTTOM_TOP,
        intArrayOf(color, color),
    ).apply {
        cornerRadius = 64f
        val size = 128
        setSize(size, size)
    }

    class DeferredResourceViewHolder(
        val root: DeferredResourceView
    ) : RecyclerView.ViewHolder(root)

    private enum class ViewType {
        COLORS, PLURALS, DRAWABLES
    }
}
