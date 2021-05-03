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

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredTextAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun constructWithAnnotatedString_resolvesAnnotatedString() {
        val originalValue = AnnotatedString(
            text = "Test string",
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
        )
        val deferred = DeferredText.Constant(originalValue)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedAnnotatedString(deferred),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(originalValue)
    }

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredText.Resource(R.string.plainString)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedAnnotatedString(deferred),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(AnnotatedString("A string"))
    }

    @Test fun resolveToString_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredText.Resource(R.string.plainString)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedString(deferred),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals("A string")
    }
}
