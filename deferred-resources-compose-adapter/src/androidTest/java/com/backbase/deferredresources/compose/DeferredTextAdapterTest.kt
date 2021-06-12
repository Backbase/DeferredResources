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

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import com.backbase.deferredresources.compose.test.assertGenericValueSemanticallyMatches
import com.google.common.truth.Truth.assertThat
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

    @Test fun rememberResolvedAnnotatedString_resolvingStyledResourceString_retainsStyleElements() {
        val deferred = DeferredText.Resource(
            resId = com.backbase.deferredresources.demo.core.R.string.styled_string,
            type = DeferredText.Resource.Type.TEXT,
        )
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedAnnotatedString(deferred),
                modifier = TestTagModifier,
            )
        }

        val space = " "
        val expected = AnnotatedString(
            text = """
                Styled text is supported:$space

                Bold, italic, underlined, struck through, and colorful.$space

                Sans-serif, serif, cursive, or monospace.$space

                Superscript or subscript, big or small.$space

                These styles can also be combined:$space
                y = x2 - 4x + 7$space
            """.trimIndent(),
            spanStyles = listOf(
                AnnotatedString.Range(item = SpanStyle(fontWeight = FontWeight.Bold), start = 28, end = 33),
                AnnotatedString.Range(item = SpanStyle(fontStyle = FontStyle.Italic), start = 34, end = 41),
                AnnotatedString.Range(item = SpanStyle(fontStyle = FontStyle.Italic), start = 208, end = 209),
                AnnotatedString.Range(item = SpanStyle(fontStyle = FontStyle.Italic), start = 212, end = 213),
                AnnotatedString.Range(item = SpanStyle(fontStyle = FontStyle.Italic), start = 218, end = 219),
                AnnotatedString.Range(
                    item = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = 42,
                    end = 53,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(textDecoration = TextDecoration.LineThrough),
                    start = 54,
                    end = 69,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(baselineShift = BaselineShift(multiplier = 0.5f)),
                    start = 130,
                    end = 135,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(baselineShift = BaselineShift(multiplier = 0.5f)),
                    start = 213,
                    end = 214,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(baselineShift = BaselineShift(multiplier = -0.5f)),
                    start = 145,
                    end = 148,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(
                        color = Color(
                            red = 0xf2,
                            green = 0x78,
                            blue = 0x0c,
                            // Color incorrectly has alpha 0 on Lollipop:
                            alpha = if (Build.VERSION.SDK_INT < 23) 0x00 else 0xff,
                        )
                    ),
                    start = 74,
                    end = 83
                ),
                AnnotatedString.Range(item = SpanStyle(fontFamily = FontFamily.Cursive), start = 105, end = 113),
                AnnotatedString.Range(item = SpanStyle(fontFamily = FontFamily.Monospace), start = 117, end = 127),
                AnnotatedString.Range(item = SpanStyle(fontFamily = FontFamily.SansSerif), start = 86, end = 97),
                AnnotatedString.Range(item = SpanStyle(fontFamily = FontFamily.Serif), start = 98, end = 104),
                AnnotatedString.Range(item = SpanStyle(fontFamily = FontFamily.Serif), start = 208, end = 223),
                AnnotatedString.Range(
                    item = SpanStyle(textGeometricTransform = TextGeometricTransform(scaleX = 1.25f, skewX = 0f)),
                    start = 156,
                    end = 159,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(textGeometricTransform = TextGeometricTransform(scaleX = 0.8f, skewX = 0f)),
                    start = 163,
                    end = 168,
                ),
                AnnotatedString.Range(
                    item = SpanStyle(textGeometricTransform = TextGeometricTransform(scaleX = 0.8f, skewX = 0f)),
                    start = 213,
                    end = 214,
                ),
            ),
        )
        composeTestRule.onNodeWithTag(TestTag).assertGenericValueSemanticallyMatches<AnnotatedString> { value ->
            assertThat(value.text).isEqualTo(expected.text)
            assertThat(value.spanStyles).hasSize(expected.spanStyles.size)
            // `inOrder` is intentionally not called because we do not care about the order:
            assertThat(value.spanStyles).containsExactlyElementsIn(expected.spanStyles)
        }
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
