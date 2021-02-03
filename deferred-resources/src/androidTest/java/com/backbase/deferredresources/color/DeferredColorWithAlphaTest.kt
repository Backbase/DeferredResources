package com.backbase.deferredresources.color

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.alpha
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

internal class DeferredColorWithAlphaTest {

    @get:Rule val parcelableTester = ParcelableTester()

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
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredColor>(
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
            parcelableTester.testParcelableThroughBundle<ParcelableDeferredColor>(withAlpha)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $base")
    }
}
