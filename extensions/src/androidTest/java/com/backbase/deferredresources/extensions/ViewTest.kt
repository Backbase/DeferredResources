package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.drawable.asDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ViewTest {

    @Test fun setBackground_setsResolvedBackground() = onView<View> {
        val deferred = DeferredColor.Constant(Color.BLUE).asDrawable()
        setBackground(deferred)

        val background = background as ColorDrawable
        assertThat(background.color).isEqualTo(Color.BLUE)
    }

    @Test fun setBackgroundColor_setsResolvedBackgroundColor() = onView<View> {
        val deferred = DeferredColor.Constant(Color.GREEN)
        setBackgroundColor(deferred)

        val background = background
        assertThat(background).isInstanceOf(ColorDrawable::class.java)
        background as ColorDrawable
        assertThat(background.color).isEqualTo(Color.GREEN)
    }

    @Test fun setMinimumWidth_setsResolvedAsSizeMinimumWidth() = onView<View> {
        val deferred = DeferredDimension.Constant(14.6f)
        setMinimumWidth(deferred)

        assertThat(minimumWidth).isEqualTo(15)
    }

    @Test fun setMinimumHeight_setsResolvedAsSizeMinimumHeight() = onView<View> {
        val deferred = DeferredDimension.Constant(14.6f)
        setMinimumHeight(deferred)

        assertThat(minimumHeight).isEqualTo(15)
    }

    @Test fun setPadding_allArguments_setsAllPaddingResolvedAsSize() = onView<View> {
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

    @Test fun setPadding_noArguments_leavesAllPaddingInPlace() = onView<View> {
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

    @Test fun setPaddingRelative_allArguments_setsAllPaddingResolvedAsSize() = onView<View> {
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

    @Test fun setPaddingRelative_noArguments_leavesAllPaddingInPlace() = onView<View> {
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

    @Test fun setTranslationX_setsExactTranslationX() = onView<View> {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationX(deferred)

        assertThat(translationX).isEqualTo(14.6f)
    }

    @Test fun setTranslationY_setsExactTranslationY() = onView<View> {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationY(deferred)

        assertThat(translationY).isEqualTo(14.6f)
    }

    @Test fun setTranslationZ_setsExactTranslationZ() = onView<View> {
        val deferred = DeferredDimension.Constant(14.6f)
        setTranslationZ(deferred)

        assertThat(translationZ).isEqualTo(14.6f)
    }

    @Test fun offsetLeftAndRight_setsResolvedOffsets() = onView<View> {
        val originalLeft = left
        val originalRight = right

        val deferred = DeferredDimension.Constant(14.6f)
        offsetLeftAndRight(deferred)

        assertThat(left).isEqualTo(originalLeft + 14)
        assertThat(right).isEqualTo(originalRight + 14)
    }

    @Test fun offsetTopAndBottom_setsResolvedOffsets() = onView<View> {
        val originalTop = top
        val originalBottom = bottom

        val deferred = DeferredDimension.Constant(14.6f)
        offsetTopAndBottom(deferred)

        assertThat(top).isEqualTo(originalTop + 14)
        assertThat(bottom).isEqualTo(originalBottom + 14)
    }

    @Test fun setFadingEdgeLength_setsResolvedSizeEdgeLength() = onView<View> {
        isHorizontalFadingEdgeEnabled = true
        isVerticalFadingEdgeEnabled = true

        val deferred = DeferredDimension.Constant(14.6f)
        setFadingEdgeLength(deferred)

        assertThat(horizontalFadingEdgeLength).isEqualTo(15)
        assertThat(verticalFadingEdgeLength).isEqualTo(15)
    }

    @Test fun setCameraDistance_setsExactResolvedDistance() = onView<View> {
        val deferred = DeferredDimension.Constant(16.9f)
        setCameraDistance(deferred)

        assertThat(cameraDistance).isEqualTo(16.9f)
    }

    @Test fun setX_setsExactResolvedPosition() = onView<View> {
        translationX = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setX(deferred)

        assertThat(x).isEqualTo(16.9f)
    }

    @Test fun setY_setsExactResolvedPosition() = onView<View> {
        translationY = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setY(deferred)

        assertThat(y).isEqualTo(16.9f)
    }

    @Test fun setZ_setsExactResolvedPosition() = onView<View> {
        translationZ = 12.3f
        val deferred = DeferredDimension.Constant(16.9f)
        setZ(deferred)

        assertThat(z).isEqualTo(16.9f)
    }

    @Test fun setElevation_setsExactResolvedElevation() = onView<View> {
        val deferred = DeferredDimension.Constant(16.9f)
        setElevation(deferred)

        assertThat(elevation).isEqualTo(16.9f)
    }
}
