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

package com.backbase.deferredresources

import androidx.annotation.Px
import com.backbase.deferredresources.dimension.ParcelableDeferredDimension
import com.backbase.deferredresources.internal.toSize
import com.backbase.deferredresources.test.AppCompatContext
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import kotlin.math.roundToInt
import org.junit.Test

internal class DeferredDimensionTest {

    //region Constant

    //region PX
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

    @Test fun constant_parcels() {
        testParcelable<ParcelableDeferredDimension>(DeferredDimension.Constant(5.5f))
    }
    //endregion

    //region DP
    private val density: Float
        get() = context.resources.displayMetrics.density

    @Test fun constantResolveAsSize_normalDpValue_returnsRoundedValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo((dpValue * scaledDensity).toSize())
    }

    @Test fun constantResolveAsSize_lowDpValue_returnsOne() {
        val deferred = DeferredDimension.Constant(0.01f, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(1)
    }

    @Test fun constantResolveAsSize_zeroDpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsSize_lowNegativeDpValue_returnsNegativeOne() {
        val deferred = DeferredDimension.Constant(-0.01f, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(-1)
    }

    @Test fun constantResolveAsOffset_normalDpValue_returnsTruncatedValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo((dpValue * scaledDensity).toInt())
    }

    @Test fun constantResolveAsOffset_lowDpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0.03f, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_zeroDpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_lowNegativeDpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(-0.049f, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveExact_normalDpValue_returnsExactValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constantResolveExact_lowDpValue_returnsExactValue() {
        val dpValue = 0.03f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constantResolveExact_zeroDpValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveExact(context)).isEqualTo(0f)
    }

    @Test fun constantResolveExact_lowNegativeDpValue_returnsExactValue() {
        val dpValue = -0.049f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.DP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constant_dp_parcels() {
        testParcelable<ParcelableDeferredDimension>(
            DeferredDimension.Constant(7.7f, DeferredDimension.Constant.Unit.DP)
        )
    }
    //endregion

    //region SP
    private val scaledDensity: Float
        get() = context.resources.displayMetrics.scaledDensity

    @Test fun constantResolveAsSize_normalSpValue_returnsRoundedValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo((dpValue * scaledDensity).toSize())
    }

    @Test fun constantResolveAsSize_lowSpValue_returnsOne() {
        val deferred = DeferredDimension.Constant(0.01f, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(1)
    }

    @Test fun constantResolveAsSize_zeroSpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsSize_lowNegativeSpValue_returnsNegativeOne() {
        val deferred = DeferredDimension.Constant(-0.01f, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsSize(context)).isEqualTo(-1)
    }

    @Test fun constantResolveAsOffset_normalSpValue_returnsTruncatedValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo((dpValue * scaledDensity).toInt())
    }

    @Test fun constantResolveAsOffset_lowSpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0.03f, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_zeroSpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveAsOffset_lowNegativeSpValue_returnsZero() {
        val deferred = DeferredDimension.Constant(-0.049f, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveAsOffset(context)).isEqualTo(0)
    }

    @Test fun constantResolveExact_normalSpValue_returnsExactValue() {
        val dpValue = 9.6f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constantResolveExact_lowSpValue_returnsExactValue() {
        val dpValue = 0.03f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constantResolveExact_zeroSpValue_returnsExactValue() {
        val deferred = DeferredDimension.Constant(0, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveExact(context)).isEqualTo(0f)
    }

    @Test fun constantResolveExact_lowNegativeSpValue_returnsExactValue() {
        val dpValue = -0.049f
        val deferred = DeferredDimension.Constant(dpValue, DeferredDimension.Constant.Unit.SP)
        assertThat(deferred.resolveExact(context)).isEqualTo(dpValue * scaledDensity)
    }

    @Test fun constant_sp_parcels() {
        testParcelable<ParcelableDeferredDimension>(
            DeferredDimension.Constant(9.9f, DeferredDimension.Constant.Unit.SP)
        )
    }
    //endregion

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
