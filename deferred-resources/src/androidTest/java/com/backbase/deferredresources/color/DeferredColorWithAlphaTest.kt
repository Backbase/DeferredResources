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

package com.backbase.deferredresources.color

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.alpha
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

internal class DeferredColorWithAlphaTest {

    private val baseColor = ColorUtils.setAlphaComponent(Color.YELLOW, 0x33)
    private val baseDeferred = DeferredColor.Constant(baseColor)

    private val disabledState = intArrayOf(-android.R.attr.state_enabled)
    private val defaultState = intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked)

    //region withAlpha
    @Test fun withAlpha_withInt_resolvesWithSpecifiedAlpha() {
        val deferred = baseDeferred.withAlpha(0xCC)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xCC)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }

    @Test fun withAlpha_withFloatMiddle_resolvesWithSpecifiedAlpha() {
        val deferred = baseDeferred.withAlpha(0.75f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xBF)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }

    @Test fun withAlpha_withFloat1_resolvesWithSpecifiedAlpha() {
        val deferred = baseDeferred.withAlpha(1f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xFF)
        assertThat(resolved).isEqualTo(Color.YELLOW)
    }

    @Test fun withAlpha_withFloat0_resolvesWithSpecifiedAlpha() {
        val deferred = baseDeferred.withAlpha(0f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0x00)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }
    //endregion

    //region Constructed
    @Test fun constructed_withInt_resolvesWithSpecifiedAlpha() {
        val deferred = DeferredColorWithAlpha(baseDeferred, alpha = 0xCC)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xCC)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }

    @Test fun constructed_withFloatMiddle_resolvesWithSpecifiedAlpha() {
        val deferred = DeferredColorWithAlpha(baseDeferred, alpha = 0.75f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xBF)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }

    @Test fun constructed_withFloat1_resolvesWithSpecifiedAlpha() {
        val deferred = DeferredColorWithAlpha(baseDeferred, alpha = 1f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0xFF)
        assertThat(resolved).isEqualTo(Color.YELLOW)
    }

    @Test fun constructed_withFloat0_resolvesWithSpecifiedAlpha() {
        val deferred = DeferredColorWithAlpha(baseDeferred, alpha = 0f)
        val resolved = deferred.resolve(context)

        assertThat(resolved.alpha).isEqualTo(0x00)
        assertThat(ColorUtils.setAlphaComponent(resolved, 0xFF)).isEqualTo(Color.YELLOW)
    }
    //endregion

    @Test fun resourceResolveToStateList_withSelectorColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Resource(R.color.stateful_color_without_attr).withAlpha(0.5f)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#80dbdbdb"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#80aaaaaa"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#80aaaaaa"))
    }

    @Test fun withParcelableBase_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredColor>(
            DeferredColorWithAlpha(DeferredColor.Constant("#bdbdbd"), 0x80)
        )
    }

    @Test fun withNonParcelableBase_throwsWhenMarshalled() {
        val base = object : DeferredColor {
            @ColorInt override fun resolve(context: Context): Int = Color.parseColor("#99101010")
            override fun resolveToStateList(context: Context): ColorStateList = ColorStateList.valueOf(resolve(context))
        }

        // Construction and resolution work normally:
        val withAlpha = DeferredColorWithAlpha(base, 0xFF)
        assertThat(withAlpha.resolve(context)).isEqualTo(Color.parseColor("#FF101010"))

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredColor>(withAlpha)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $base")
    }
}
