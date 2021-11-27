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
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.ExperimentalComposeAdapter
import com.backbase.deferredresources.compose.rememberResolvedAnnotatedString
import com.backbase.deferredresources.demo.core.SamplesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.android.material.composethemeadapter.MdcTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalComposeAdapter::class,
    ExperimentalPagerApi::class,
)
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
            val viewModel = viewModel<SamplesViewModel>()
            Column {
                val pagerState by rememberSaveable(
                    saver = Saver(
                        save = { it.value.currentPage },
                        restore = { mutablePagerState(it) },
                    )
                ) {
                    mutablePagerState(0)
                }

                Surface(elevation = 4.dp) {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        backgroundColor = MaterialTheme.colors.primary,
                    ) {
                        val coroutineScope = rememberCoroutineScope()
                        SampleTab(
                            text = viewModel.colorSamplesTitle,
                            index = 0,
                            pagerState = pagerState,
                            coroutineScope = coroutineScope,
                        )
                        SampleTab(
                            text = viewModel.formattedPluralsSampleTitle,
                            index = 1,
                            pagerState = pagerState,
                            coroutineScope = coroutineScope,
                        )
                        SampleTab(
                            text = viewModel.iconSamplesTitle,
                            index = 2,
                            pagerState = pagerState,
                            coroutineScope = coroutineScope,
                        )
                        SampleTab(
                            text = viewModel.textSampleTitle,
                            index = 3,
                            pagerState = pagerState,
                            coroutineScope = coroutineScope,
                        )
                    }
                }

                HorizontalPager(
                    count = 4,
                    state = pagerState,
                ) { page ->
                    when (page) {
                        0 -> ColorSamplesPage(viewModel.colorSamples)
                        1 -> PluralsSamplePage(viewModel.formattedPluralsSample)
                        2 -> IconSamplesPage(viewModel.iconSamples)
                        3 -> TextSamplePage(viewModel.textSample)
                    }
                }
            }
        }
    }

    @Composable
    private fun SampleTab(
        text: DeferredText,
        index: Int,
        pagerState: PagerState,
        coroutineScope: CoroutineScope,
    ) = Tab(
        selected = index == pagerState.currentPage,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(page = index)
            }
        },
        text = {
            Text(text = rememberResolvedAnnotatedString(text))
        },
    )

    private fun mutablePagerState(currentPage: Int = 0) = mutableStateOf(
        PagerState(
            currentPage = currentPage,
        )
    )
}
