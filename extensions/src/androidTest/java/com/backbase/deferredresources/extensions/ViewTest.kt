package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.drawable.asDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ViewTest {

    @Test fun setBackground_setsResolvedBackground() = onView {
        val deferred = DeferredColor.Constant(Color.BLUE).asDrawable()
        setBackground(deferred)

        val background = background as ColorDrawable
        assertThat(background.color).isEqualTo(Color.BLUE)
    }

    @Test fun setBackgroundColor_setsResolvedBackgroundColor() = onView {
        val deferred = DeferredColor.Constant(Color.GREEN)
        setBackgroundColor(deferred)

        val background = background
        assertThat(background).isInstanceOf(ColorDrawable::class.java)
        background as ColorDrawable
        assertThat(background.color).isEqualTo(Color.GREEN)
    }

    @Test fun setMinimumWidth_setsResolvedAsSizeMinimumWidth() = onView {
        val deferred = DeferredDimension.Constant(14.6f)
        setMinimumWidth(deferred)

        assertThat(minimumWidth).isEqualTo(15)
    }

    @Test fun setMinimumHeight_setsResolvedAsSizeMinimumHeight() = onView {
        val deferred = DeferredDimension.Constant(14.6f)
        setMinimumHeight(deferred)

        assertThat(minimumHeight).isEqualTo(15)
    }

    @Test fun setPadding_allArguments_setsAllPaddingResolvedAsSize() = onView {
        val left = DeferredDimension.Constant(0.3f)
        val top = DeferredDimension.Constant(1.7f)
        val right = DeferredDimension.Constant(2.7f)
        val bottom = DeferredDimension.Constant(3.7f)
        setPadding(left, top, right, bottom)

        assertThat(paddingLeft).isEqualTo(1)
        assertThat(paddingTop).isEqualTo(2)
        assertThat(paddingRight).isEqualTo(3)
        assertThat(paddingBottom).isEqualTo(4)
    }

    @Test fun setPadding_noArguments_leavesAllPaddingInPlace() = onView {
        // Set up:
        setPadding(1, 2, 3, 4)

        // Test function:
        setPadding()

        // Verify:
        assertThat(paddingLeft).isEqualTo(1)
        assertThat(paddingTop).isEqualTo(2)
        assertThat(paddingRight).isEqualTo(3)
        assertThat(paddingBottom).isEqualTo(4)
    }

    @Test fun setPaddingRelative_allArguments_setsAllPaddingResolvedAsSize() = onView {
        layoutDirection = View.LAYOUT_DIRECTION_RTL

        val start = DeferredDimension.Constant(0.3f)
        val top = DeferredDimension.Constant(1.7f)
        val end = DeferredDimension.Constant(2.7f)
        val bottom = DeferredDimension.Constant(3.7f)
        setPaddingRelative(start, top, end, bottom)

        assertThat(paddingStart).isEqualTo(1)
        assertThat(paddingTop).isEqualTo(2)
        assertThat(paddingEnd).isEqualTo(3)
        assertThat(paddingBottom).isEqualTo(4)
    }

    @Test fun setPaddingRelative_noArguments_leavesAllPaddingInPlace() = onView {
        // Set up:
        layoutDirection = View.LAYOUT_DIRECTION_RTL
        setPadding(1, 2, 3, 4)

        // Test function:
        setPaddingRelative()

        // Verify:
        assertThat(paddingLeft).isEqualTo(1)
        assertThat(paddingTop).isEqualTo(2)
        assertThat(paddingRight).isEqualTo(3)
        assertThat(paddingBottom).isEqualTo(4)
    }

    @Test fun setTranslationX_setsExactTranslationX() = onView {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationX(deferred)

        assertThat(translationX).isEqualTo(14.6f)
    }

    @Test fun setTranslationY_setsExactTranslationY() = onView {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationY(deferred)

        assertThat(translationY).isEqualTo(14.6f)
    }

    @Test fun setTranslationZ_setsExactTranslationZ() = onView {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationZ(deferred)

        assertThat(translationZ).isEqualTo(14.6f)
    }

    @Test fun offsetLeftAndRight_setsResolvedOffsets() = onView {
        val originalLeft = left
        val originalRight = right

        val deferred = DeferredDimension.Constant(14.6f)
        offsetLeftAndRight(deferred)

        assertThat(left).isEqualTo(originalLeft + 14)
        assertThat(right).isEqualTo(originalRight + 14)
    }

    @Test fun offsetTopAndBottom_setsResolvedOffsets() = onView {
        val originalTop = top
        val originalBottom = bottom

        val deferred = DeferredDimension.Constant(14.6f)
        offsetTopAndBottom(deferred)

        assertThat(top).isEqualTo(originalTop + 14)
        assertThat(bottom).isEqualTo(originalBottom + 14)
    }

    @Test fun setFadingEdgeLength_setsResolvedSizeEdgeLength() = onView {
        isHorizontalFadingEdgeEnabled = true
        isVerticalFadingEdgeEnabled = true

        val deferred = DeferredDimension.Constant(14.6f)
        setFadingEdgeLength(deferred)

        assertThat(horizontalFadingEdgeLength).isEqualTo(15)
        assertThat(verticalFadingEdgeLength).isEqualTo(15)
    }

    @Test fun setCameraDistance_setsExactResolvedDistance() = onView {
        val deferred = DeferredDimension.Constant(16.9f)
        setCameraDistance(deferred)

        assertThat(cameraDistance).isEqualTo(16.9f)
    }

    @Test fun setX_setsExactResolvedPosition() = onView {
        translationX = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setX(deferred)

        assertThat(x).isEqualTo(16.9f)
    }

    @Test fun setY_setsExactResolvedPosition() = onView {
        translationY = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setY(deferred)

        assertThat(y).isEqualTo(16.9f)
    }

    @Test fun setZ_setsExactResolvedPosition() = onView {
        translationZ = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setZ(deferred)

        assertThat(z).isEqualTo(16.9f)
    }

    @Test fun setElevation_setsExactResolvedElevation() = onView {
        val deferred = DeferredDimension.Constant(16.9f)
        setElevation(deferred)

        assertThat(elevation).isEqualTo(16.9f)
    }

    private fun onView(
        afterIdleSync: (View.() -> Unit)? = null,
        test: View.() -> Unit
    ) {
        val scenario = ActivityScenario.launch(Activity::class.java)
        scenario.onActivity { it.view.test() }

        if (afterIdleSync != null) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.onActivity { it.view.afterIdleSync() }
        }
    }

    class Activity : android.app.Activity() {

        lateinit var view: View
            private set

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            view = View(this).apply {
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
