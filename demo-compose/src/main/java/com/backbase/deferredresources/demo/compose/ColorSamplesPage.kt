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

package com.backbase.deferredresources.demo.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.ExperimentalComposeAdapter
import com.backbase.deferredresources.compose.rememberResolvedAnnotatedString
import com.backbase.deferredresources.compose.rememberResolvedColor
import com.backbase.deferredresources.demo.core.ColorSample

@Preview
@Composable
private fun ColorSamplesPage() = ColorSamplesPage(
    colorSamples = listOf(
        ColorSample(
            color = DeferredColor.Constant(android.graphics.Color.RED),
            description = DeferredText.Constant("Red"),
        ),
        ColorSample(
            color = DeferredColor.Constant(android.graphics.Color.BLUE),
            description = DeferredText.Constant("Blue"),
        ),
    ),
)

@OptIn(ExperimentalComposeAdapter::class)
@Composable
fun ColorSamplesPage(
    colorSamples: List<ColorSample>,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier.fillMaxSize()
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
    }

    items(colorSamples) { sample ->
        val color = rememberResolvedColor(sample.color)
        val contentColor = rememberCalculatedContentColor(color)

        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            color = color,
            contentColor = contentColor,
            elevation = 2.dp,
        ) {
            Text(
                text = rememberResolvedAnnotatedString(sample.description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
        }
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable private fun rememberCalculatedContentColor(backgroundColor: Color): Color {
    val opaqueColor = backgroundColor.copy(alpha = 1f)
    val localContentAlpha = LocalContentAlpha.current
    return remember(opaqueColor) {
        val blackContentColor = Color.Black.copy(alpha = localContentAlpha)
        val blackContentContrast = ColorUtils.calculateContrast(blackContentColor.toArgb(), opaqueColor.toArgb())

        val whiteContentColor = Color.White.copy(alpha = localContentAlpha)
        val whiteContentContrast = ColorUtils.calculateContrast(whiteContentColor.toArgb(), opaqueColor.toArgb())

        if (whiteContentContrast > blackContentContrast) whiteContentColor else blackContentColor
    }
}
