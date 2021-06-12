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

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.demo.core.SamplesViewModel

class DemoPagerAdapter(
    private val viewModel: SamplesViewModel,
) : RecyclerView.Adapter<DemoPagerAdapter.DeferredResourceViewHolder>() {

    fun getPageName(position: Int): DeferredText = when (position) {
        0 -> viewModel.colorSamplesTitle
        1 -> viewModel.formattedPluralsSampleTitle
        2 -> viewModel.iconSamplesTitle
        3 -> viewModel.textSampleTitle
        else -> throw IllegalArgumentException("Position $position in adapter with size $itemCount")
    }

    override fun getItemCount(): Int = ViewType.values().size

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> ViewType.COLORS.ordinal
        1 -> ViewType.PLURALS.ordinal
        2 -> ViewType.DRAWABLES.ordinal
        3 -> ViewType.STYLED_TEXT.ordinal
        else -> throw IndexOutOfBoundsException("Position $position in adapter with size $itemCount")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeferredResourceViewHolder {
        val view = when (ViewType.values()[viewType]) {
            ViewType.COLORS -> DeferredColorsView(parent.context)
            ViewType.PLURALS -> DeferredPluralsView(parent.context)
            ViewType.DRAWABLES -> DeferredDrawablesView(parent.context)
            ViewType.STYLED_TEXT -> DeferredTextView(parent.context)
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
            is DeferredColorsView -> viewModel.colorSamples.forEach { sample ->
                view.display(sample.color, sample.description)
            }
            is DeferredPluralsView -> view.display(viewModel.formattedPluralsSample)
            is DeferredDrawablesView -> viewModel.iconSamples.forEach { sample ->
                view.display(sample.icon, sample.description)
            }
            is DeferredTextView -> view.display(viewModel.textSample)
        }
    }

    class DeferredResourceViewHolder(
        val root: DeferredResourceView
    ) : RecyclerView.ViewHolder(root)

    private enum class ViewType {
        COLORS, PLURALS, DRAWABLES, STYLED_TEXT,
    }
}
