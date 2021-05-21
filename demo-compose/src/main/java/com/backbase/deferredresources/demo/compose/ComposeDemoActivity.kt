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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.ExperimentalComposeAdapter
import com.backbase.deferredresources.compose.rememberResolvedAnnotatedString
import com.backbase.deferredresources.demo.core.SamplesViewModel
import com.google.android.material.composethemeadapter.MdcTheme

@OptIn(ExperimentalComposeAdapter::class)
class ComposeDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun Content() = MdcTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            val viewModel = remember { SamplesViewModel() }
            Column {
                var selectedTabIndex by mutableStateOf(0)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.primary,
                    elevation = 4.dp,
                ) {
                    TabRow(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        selectedTabIndex = selectedTabIndex,
                    ) {
                        SampleTab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            viewModel.colorSamplesTitle,
                        )
                        SampleTab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            viewModel.formattedPluralsSampleTitle,
                        )
                        SampleTab(
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 },
                            viewModel.iconSamplesTitle,
                        )
                    }
                }

                // TODO: Pager
            }
        }
    }

    @Composable
    private fun SampleTab(
        selected: Boolean,
        onClick: () -> Unit,
        text: DeferredText,
    ) = Tab(
        selected = selected,
        onClick = onClick,
    ) {
        Text(text = rememberResolvedAnnotatedString(text))
    }
}
