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

package com.backbase.deferredresources.compose

import androidx.compose.material.Text
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.backbase.deferredresources.DeferredTypeface
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredTypefaceAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun rememberResolvedPainter_paintsDrawableAndHasExpectedSize() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)

        composeTestRule.setContent {
            val fontFamily = rememberResolvedFontFamily(deferred)
            Text(
                text = "DeferredTypeface test",
                modifier = TestTagModifier,
                fontFamily = fontFamily,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertTextEquals("DeferredTypeface test")
        // TODO: Assert something about the typeface here. We don't have public access to any properties of FontFamily.
    }
}
