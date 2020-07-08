package com.backbase.deferredresources.color

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.alpha
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.context
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredColorWithAlphaTest {

    private val baseColor = ColorUtils.setAlphaComponent(Color.YELLOW, 0x33)
    private val baseDeferred = DeferredColor.Constant(baseColor)

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
}
