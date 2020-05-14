package com.backbase.deferredresources

import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredDimensionTest {

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
    //endregion
}
