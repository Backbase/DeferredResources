package com.backbase.deferredresources

import androidx.annotation.Px
import com.backbase.deferredresources.dimension.ParcelableDeferredDimension
import com.backbase.deferredresources.test.AppCompatContext
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import kotlin.math.roundToInt
import org.junit.Test

internal class DeferredDimensionTest {

    //region Constant
    @Test fun constantResolveAsSize_normalPxValue_returnsRoundedValue() {
        val deferred = DeferredDimension.Constant(9.6f)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(10)
    }

    @Test fun constantResolveAsSize_lowPxValue_returnsOne() {
        val deferred = DeferredDimension.Constant(0.3f)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(1)
    }

    @Test fun constantResolveAsSize_zeroPxValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsSize_lowNegativePxValue_returnsNegativeOne() {
        val deferred = DeferredDimension.Constant(-0.49f)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(-1)
    }

    @Test fun constantResolveAsOffset_normalPxValue_returnsTruncatedValue() {
        val deferred = DeferredDimension.Constant(9.6f)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(9)
    }

    @Test fun constantResolveAsOffset_lowPxValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0.3f)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_zeroPxValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_lowNegativePxValue_returnsZero() {
        val deferred = DeferredDimension.Constant(-0.49f)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveExact_normalPxValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(9.6f)
        assertThat(deferred.resolveExact(context)).isEqualTo(9.6f)
    }

    @Test fun constantResolveExact_lowPxValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(0.3f)
        assertThat(deferred.resolveExact(context)).isEqualTo(0.3f)
    }

    @Test fun constantResolveExact_zeroPxValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(0)
        assertThat(deferred.resolveExact(context)).isEqualTo(0f)
    }

    @Test fun constantResolveExact_lowNegativePxValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(-0.49f)
        assertThat(deferred.resolveExact(context)).isEqualTo(-0.49f)
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredDimension>(DeferredDimension.Constant(5.5f))
    }
    //endregion

    //region Resource
    @Test fun resourceResolveAsSize_roundsWithMinValueOne() {
        val deferred = DeferredDimension.Resource(R.dimen.testDimen)

        assertThat(deferred.resolveAsSize(context)).isEqualTo(1)
    }

    @Test fun resourceResolveAsOffset_truncatesToZero() {
        val deferred = DeferredDimension.Resource(R.dimen.testDimen)

        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun resourceResolveExact_returnsExactInPx() {
        val deferred = DeferredDimension.Resource(R.dimen.testDimen)

        val oneQuarterDpAsPx = 0.25f * context.resources.displayMetrics.density

        assertThat(deferred.resolveExact(context)).isEqualTo(oneQuarterDpAsPx)
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredDimension>(DeferredDimension.Resource(R.dimen.testDimen))
    }
    //endregion

    //region Attribute
    @Test fun attributeResolveAsSize_resolvesAsPxSize() {
        val deferred = DeferredDimension.Attribute(R.attr.actionBarSize)

        assertThat(deferred.resolveAsSize(AppCompatContext())).isEqualTo(56.dp)
    }

    @Test fun attributeResolveAsOffset_resolvesAsPxInt() {
        val deferred = DeferredDimension.Attribute(R.attr.actionBarSize)

        assertThat(deferred.resolveAsOffset(AppCompatContext())).isEqualTo(56.dp)
    }

    @Test fun attributeResolveExact_resolvesExactPixels() {
        val deferred = DeferredDimension.Attribute(R.attr.actionBarSize)

        assertThat(deferred.resolveExact(AppCompatContext())).isEqualTo(56f.dp)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolveExact_withUnknownAttribute_throwsException() {
        val deferred = DeferredDimension.Attribute(R.attr.actionBarSize)

        // Default-theme context does not have <actionBarSize> attribute:
        deferred.resolveExact(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolveExact_withWrongAttributeType_throwsException() {
        val deferred = DeferredDimension.Attribute(R.attr.isLightTheme)

        deferred.resolveExact(AppCompatContext())
    }

    @Test fun attribute_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredDimension>(DeferredDimension.Attribute(R.attr.actionBarSize))
    }
    //endregion

    @get:Px
    private val Int.dp: Int
        get() = toFloat().dp.roundToInt()

    @get:Px
    private val Float.dp: Float
        get() = this * context.resources.displayMetrics.density
}
