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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.ExperimentalComposeAdapter
import com.backbase.deferredresources.compose.rememberResolvedAnnotatedString
import com.backbase.deferredresources.compose.rememberResolvedPainter
import com.backbase.deferredresources.compose.rememberResolvedString
import com.backbase.deferredresources.demo.core.IconSample

@Preview
@Composable
private fun IconSamplesPage() = IconSamplesPage(
    iconSamples = listOf(
        IconSample(
            icon = DeferredDrawable.Resource(R.drawable.ic_flower_24),
            description = DeferredText.Constant("Flower icon"),
        )
    )
)

@OptIn(ExperimentalComposeAdapter::class)
@Composable
fun IconSamplesPage(
    iconSamples: List<IconSample>,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier.fillMaxSize()
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
    }

    items(iconSamples) { sample ->
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = rememberResolvedPainter(sample.icon),
                contentDescription = rememberResolvedString(sample.description),
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(8.dp),
                tint = Color.Unspecified,
            )
            Text(
                text = rememberResolvedAnnotatedString(sample.description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.h5,
            )
        }
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}
