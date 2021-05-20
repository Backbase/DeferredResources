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

package com.backbase.deferredresources.viewx

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.DeferredTypeface
import com.backbase.deferredresources.viewx.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assume.assumeFalse
import org.junit.Test

internal class TextViewTest {

    @Test fun setText_displaysResolvedText() =
        onView<TextView> {
            val deferred = DeferredText.Constant("Deferred")
            setText(deferred)

            assertThat(text).isEqualTo("Deferred")
        }

    @Test fun setText_withBufferType_displaysResolvedText() =
        onViewInActivity<TextView> {
            assertThat(editableText).isNull()

            val deferred = DeferredText.Constant("Deferred")
            setText(deferred, TextView.BufferType.EDITABLE)

            assertThat(text.toString()).isEqualTo("Deferred")
            assertThat(editableText).isNotNull()
            assertThat(editableText.toString()).isEqualTo("Deferred")
        }

    @Test fun setHint_displaysResolvedHint() =
        onView<TextView> {
            val deferred = DeferredText.Constant("Deferred")
            setHint(deferred)

            assertThat(hint).isEqualTo("Deferred")
        }

    @Test fun setTextColor_displaysResolvedColor() =
        onView<TextView> {
            val deferred = DeferredColor.Constant(Color.GREEN)
            setTextColor(deferred)

            assertThat(currentTextColor).isEqualTo(Color.GREEN)
        }

    @Test fun setHintTextColor_displaysResolvedColor() =
        onView<TextView> {
            val deferred = DeferredColor.Constant(Color.LTGRAY)
            setHintTextColor(deferred)

            assertThat(currentHintTextColor).isEqualTo(Color.LTGRAY)
        }

    @Test fun setTypeface_displaysResolvedTypeface() =
        onView<TextView> {
            val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
            setTypeface(deferred)

            assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
            if (Build.VERSION.SDK_INT >= 28)
                assertThat(typeface.weight).isEqualTo(300)
            assertThat(typeface.style).isEqualTo(Typeface.ITALIC)
        }

    @Test fun setTypeface_withStyle_displaysResolvedTypefaceWithStyle() =
        onView<TextView> {
            val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
            setTypeface(deferred, Typeface.BOLD)

            assumeFalse("Typeface style is not changed on API < 21", Build.VERSION.SDK_INT < 21)
            assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
            if (Build.VERSION.SDK_INT >= 28)
                assertThat(typeface.weight).isEqualTo(600)
            assertThat(typeface.style).isEqualTo(Typeface.BOLD)
        }

    @Test fun setTextSize_displaysResolvedSize() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 888)
            setTextSize(deferred)

            assertThat(textSize).isEqualTo(888f)
        }

    @Test fun setAutoSizeTextTypeUniformWithConfiguration_displaysWithResolvedConfiguration() =
        onView<AppCompatTextView> {
            val deferredMin = DeferredDimension.Constant(pxValue = 0.3f)
            val deferredMax = DeferredDimension.Constant(pxValue = 55.7f)
            val deferredStep = DeferredDimension.Constant(pxValue = 5)
            setAutoSizeTextTypeUniformWithConfiguration(deferredMin, deferredMax, deferredStep)

            assertThat(autoSizeTextType).isEqualTo(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            assertThat(autoSizeMinTextSize).isEqualTo(1)
            assertThat(autoSizeMaxTextSize).isEqualTo(56)
            assertThat(autoSizeStepGranularity).isEqualTo(5)
        }

    @Test fun setLineHeight_displaysWithResolvedLineHeight() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 26)
            setLineHeight(deferred)

            assertThat(lineHeight).isEqualTo(26)
        }

    @Test fun setFirstBaselineToTopHeight_displaysWithResolvedHeight() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 99)
            setFirstBaselineToTopHeight(deferred)

            assertThat(TextViewCompat.getFirstBaselineToTopHeight(this)).isEqualTo(99)
        }

    @Test fun setLastBaselineToBottomHeight_displaysWithResolvedHeight() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 19)
            setLastBaselineToBottomHeight(deferred)

            assertThat(TextViewCompat.getLastBaselineToBottomHeight(this)).isEqualTo(19)
        }

    @Test fun setMinHeight_displaysWithResolvedMinHeight() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 21)
            setMinHeight(deferred)

            assumeFalse("Cannot verify min height on API < 16", Build.VERSION.SDK_INT < 16)
            assertThat(minHeight).isEqualTo(21)
        }

    @Test fun setMaxHeight_displaysWithResolvedMaxHeight() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 23)
            setMaxHeight(deferred)

            assumeFalse("Cannot verify max height on API < 16", Build.VERSION.SDK_INT < 16)
            assertThat(maxHeight).isEqualTo(23)
        }

    @Test fun setHeight_displaysWithResolvedHeight() =
        onViewInActivity<TextView>(
            test = {
                val deferred = DeferredDimension.Constant(pxValue = 25)
                setHeight(deferred)
            },
            afterIdleSync = {
                assertThat(height).isEqualTo(25)
            }
        )

    @Test fun setMinWidth_displaysWithResolvedMinWidth() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 27)
            setMinWidth(deferred)

            assumeFalse("Cannot verify min width on API < 16", Build.VERSION.SDK_INT < 16)
            assertThat(minWidth).isEqualTo(27)
        }

    @Test fun setMaxWidth_displaysWithResolvedMaxWidth() =
        onView<TextView> {
            val deferred = DeferredDimension.Constant(pxValue = 29)
            setMaxWidth(deferred)

            assumeFalse("Cannot verify max width on API < 16", Build.VERSION.SDK_INT < 16)
            assertThat(maxWidth).isEqualTo(29)
        }

    @Test fun setWidth_displaysWithResolvedWidth() =
        onViewInActivity<TextView>(
            test = {
                val deferred = DeferredDimension.Constant(pxValue = 31)
                setWidth(deferred)
            },
            afterIdleSync = {
                assertThat(width).isEqualTo(31)
            }
        )

    @Test fun setLineSpacing_displaysWithLineSpacing() =
        onView<TextView> {
            val deferredAdd = DeferredDimension.Constant(pxValue = 32.1f)
            setLineSpacing(deferredAdd, 2f)

            assumeFalse("Cannot verify line spacing on API < 16", Build.VERSION.SDK_INT < 16)
            assertThat(lineSpacingExtra).isEqualTo(32.1f)
            assertThat(lineSpacingMultiplier).isEqualTo(2f)
        }
}
