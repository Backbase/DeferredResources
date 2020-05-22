package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.DeferredTypeface
import com.backbase.deferredresources.extensions.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assume.assumeFalse
import org.junit.Test

class TextViewTest {

    @Test fun setText_displaysResolvedText() = onView {
        val deferred = DeferredText.Constant("Deferred")
        setText(deferred)

        assertThat(text).isEqualTo("Deferred")
    }

    @Test fun setText_withBufferType_displaysResolvedText() = onView {
        assertThat(editableText).isNull()

        val deferred = DeferredText.Constant("Deferred")
        setText(deferred, TextView.BufferType.EDITABLE)

        assertThat(text.toString()).isEqualTo("Deferred")
        assertThat(editableText).isNotNull()
        assertThat(editableText.toString()).isEqualTo("Deferred")
    }

    @Test fun setHint_displaysResolvedHint() = onView {
        val deferred = DeferredText.Constant("Deferred")
        setHint(deferred)

        assertThat(hint).isEqualTo("Deferred")
    }

    @Test fun setTextColor_displaysResolvedColor() = onView {
        val deferred = DeferredColor.Constant(Color.GREEN)
        setTextColor(deferred)

        assertThat(currentTextColor).isEqualTo(Color.GREEN)
    }

    @Test fun setHintTextColor_displaysResolvedColor() = onView {
        val deferred = DeferredColor.Constant(Color.LTGRAY)
        setHintTextColor(deferred)

        assertThat(currentHintTextColor).isEqualTo(Color.LTGRAY)
    }

    @Test fun setTypeface_displaysResolvedTypeface() = onView {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        setTypeface(deferred)

        assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
        if (Build.VERSION.SDK_INT >= 28)
            assertThat(typeface.weight).isEqualTo(300)
        assertThat(typeface.style).isEqualTo(Typeface.ITALIC)
    }

    @Test fun setTypeface_withStyle_displaysResolvedTypefaceWithStyle() = onView {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        setTypeface(deferred, Typeface.BOLD)

        assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
        if (Build.VERSION.SDK_INT >= 28)
            assertThat(typeface.weight).isEqualTo(600)
        assertThat(typeface.style).isEqualTo(Typeface.BOLD)
    }

    @Test fun setTextSize_displaysResolvedSize() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 888)
        setTextSize(deferred)

        assertThat(textSize).isEqualTo(888f)
    }

    @Test fun setAutoSizeTextTypeUniformWithConfiguration_displaysWithResolvedConfiguration() = onView {
        val deferredMin = DeferredDimension.Constant(pxValue = 0.3f)
        val deferredMax = DeferredDimension.Constant(pxValue = 55.7f)
        val deferredStep = DeferredDimension.Constant(pxValue = 5)
        setAutoSizeTextTypeUniformWithConfiguration(deferredMin, deferredMax, deferredStep)

        assertThat(autoSizeTextType).isEqualTo(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        assertThat(autoSizeMinTextSize).isEqualTo(1)
        assertThat(autoSizeMaxTextSize).isEqualTo(56)
        assertThat(autoSizeStepGranularity).isEqualTo(5)
    }

    @Test fun setLineHeight_displaysWithResolvedLineHeight() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 26)
        setLineHeight(deferred)

        assertThat(lineHeight).isEqualTo(26)
    }

    @Test fun setFirstBaselineToTopHeight_displaysWithResolvedHeight() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 99)
        setFirstBaselineToTopHeight(deferred)

        assertThat(TextViewCompat.getFirstBaselineToTopHeight(this)).isEqualTo(99)
    }

    @Test fun setLastBaselineToBottomHeight_displaysWithResolvedHeight() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 19)
        setLastBaselineToBottomHeight(deferred)

        assertThat(TextViewCompat.getLastBaselineToBottomHeight(this)).isEqualTo(19)
    }

    @Test fun setMinHeight_displaysWithResolvedMinHeight() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 21)
        setMinHeight(deferred)

        assertThat(minHeight).isEqualTo(21)
    }

    @Test fun setMaxHeight_displaysWithResolvedMaxHeight() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 23)
        setMaxHeight(deferred)

        assertThat(maxHeight).isEqualTo(23)
    }

    @Test fun setHeight_displaysWithResolvedHeight() = onView(
        test = {
            val deferred = DeferredDimension.Constant(pxValue = 25)
            setHeight(deferred)
        },
        afterIdleSync = {
            assertThat(height).isEqualTo(25)
        }
    )

    @Test fun setMinWidth_displaysWithResolvedMinWidth() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 27)
        setMinWidth(deferred)

        assertThat(minWidth).isEqualTo(27)
    }

    @Test fun setMaxWidth_displaysWithResolvedMaxWidth() = onView {
        val deferred = DeferredDimension.Constant(pxValue = 29)
        setMaxWidth(deferred)

        assertThat(maxWidth).isEqualTo(29)
    }

    @Test fun setWidth_displaysWithResolvedWidth() = onView(
        test = {
            val deferred = DeferredDimension.Constant(pxValue = 31)
            setWidth(deferred)
        },
        afterIdleSync = {
            assertThat(width).isEqualTo(31)
        }
    )

    @Test fun setLineSpacing_displaysWithLineSpacing() = onView {
        val deferredAdd = DeferredDimension.Constant(pxValue = 32.1f)
        setLineSpacing(deferredAdd, 2f)

        assertThat(lineSpacingExtra).isEqualTo(32.1f)
        assertThat(lineSpacingMultiplier).isEqualTo(2f)
    }

    private fun onView(
        afterIdleSync: (TextView.() -> Unit)? = null,
        test: TextView.() -> Unit
    ) {
        val scenario = ActivityScenario.launch(Activity::class.java)
        scenario.onActivity { it.view.test() }

        if (afterIdleSync != null) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.onActivity { it.view.afterIdleSync() }
        }
    }

    class Activity : android.app.Activity() {

        lateinit var view: AppCompatTextView
            private set

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            view = AppCompatTextView(this).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val container = FrameLayout(this).apply {
                addView(view)
            }
            setContentView(container)
        }
    }
}
