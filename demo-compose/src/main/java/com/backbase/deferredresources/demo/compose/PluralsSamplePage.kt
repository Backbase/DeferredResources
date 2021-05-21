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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.compose.ExperimentalComposeAdapter
import com.backbase.deferredresources.compose.rememberResolvedString

@Preview
@Composable
private fun PluralsSamplePage() = PluralsSamplePage(
    plurals = DeferredFormattedPlurals.Resource(R.plurals.horses),
)

@OptIn(ExperimentalComposeAdapter::class)
@Composable
fun PluralsSamplePage(
    plurals: DeferredFormattedPlurals,
    modifier: Modifier = Modifier,
) = LazyColumn(
    modifier = modifier.fillMaxSize()
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
    }

    items(listOf(0, 1, 2, 3, 14, 100)) { count ->
        Text(
            text = rememberResolvedString(plurals, quantity = count),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.h5,
        )
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}
