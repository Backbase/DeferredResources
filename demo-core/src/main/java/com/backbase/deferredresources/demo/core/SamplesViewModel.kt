/*
 * Copyright 2021 Backbase R&D B.V.
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

package com.backbase.deferredresources.demo.core

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.drawable.DrawableCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredText

/**
 * View model representing the samples shown in the demo UI. Consuming app must use
 * [R.style.Theme_DeferredResourcesDemo].
 */
public class SamplesViewModel {

    /**
     * Title of the section displaying [colorSamples].
     */
    public val colorSamplesTitle: DeferredText = DeferredText.Constant("Colors")

    /**
     * The [ColorSample]s to display.
     */
    public val colorSamples: List<ColorSample> = listOf(
        ColorSample(
            color = DeferredColor.Attribute(R.attr.colorSecondary),
            description = DeferredText.Constant("Secondary"),
        ),
        ColorSample(
            color = DeferredColor.Resource(R.color.backbase_red),
            description = DeferredText.Constant("Backbase red"),
        ),
        ColorSample(
            color = DeferredColor.Constant(Color.WHITE),
            description = DeferredText.Constant("White"),
        ),
    )

    /**
     * Title of the section displaying a [formattedPluralsSample].
     */
    public val formattedPluralsSampleTitle: DeferredText = DeferredText.Constant("Plurals resource")

    /**
     * The [DeferredFormattedPlurals] to display.
     */
    public val formattedPluralsSample: DeferredFormattedPlurals = DeferredFormattedPlurals.Resource(R.plurals.horses)

    /**
     * Title of the section displaying [iconSamples].
     */
    public val iconSamplesTitle: DeferredText = DeferredText.Constant("Drawables")

    /**
     * The [IconSample]s to display.
     */
    public val iconSamples: List<IconSample> = listOf(
        IconSample(
            icon = DeferredDrawable.Constant(GrayCircle()),
            description = DeferredText.Constant("Constant"),
        ),
        IconSample(
            icon = DeferredDrawable.Resource(R.drawable.ic_flower_24) { context ->
                setTint(DeferredColor.Resource(R.color.green), context)
            },
            description = DeferredText.Constant("Resource"),
        ),
        IconSample(
            icon = DeferredDrawable.Attribute(R.attr.demoIcon) { context ->
                setTint(DeferredColor.Attribute(R.attr.colorSecondary), context)
            },
            description = DeferredText.Constant("Attribute"),
        ),
    )

    @Suppress("FunctionName") // Factory
    private fun GrayCircle(): Drawable = GradientDrawable(
        GradientDrawable.Orientation.BOTTOM_TOP,
        intArrayOf(Color.GRAY, Color.GRAY),
    ).apply {
        cornerRadius = 64f
        val size = 128
        setSize(size, size)
    }

    private fun Drawable.setTint(color: DeferredColor, context: Context) {
        DrawableCompat.setTint(this, color.resolve(context))
    }
}
