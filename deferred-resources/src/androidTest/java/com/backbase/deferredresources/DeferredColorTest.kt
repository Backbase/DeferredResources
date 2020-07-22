package com.backbase.deferredresources

import android.graphics.Color
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredColorTest {

    //region Constant
    @Test fun constantResolve_withIntValue_returnsSameValue() {
        val deferred = DeferredColor.Constant(Color.MAGENTA)
        assertThat(deferred.resolve(context)).isEqualTo(Color.MAGENTA)
    }

    @Test fun constantResolve_withStringValue_returnsParsedValue() {
        val deferred = DeferredColor.Constant("#00FF00")
        assertThat(deferred.resolve(context)).isEqualTo(Color.GREEN)
    }
    //endregion

    //region Resource
    @Test fun resourceResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Resource(R.color.blue)
        assertThat(deferred.resolve(context)).isEqualTo(Color.BLUE)
    }

    @Test fun resourceResolve_withSelectorColor_resolvesDefaultColor() {
        val deferred = DeferredColor.Resource(R.color.stateful_color)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#987654"))
    }
    //endregion

    //region Attribute
    @Test fun attributeResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#987654"))
    }

    @Test fun attributeResolve_withSelectorColor_resolvesDefaultColor() {
        val deferred = DeferredColor.Attribute(android.R.attr.textColorPrimary)
        assertThat(deferred.resolve(context)).isEqualTo(Color.parseColor("#de000000"))
    }

    @Test fun attributeResolve_withSelectorColorWithAttributeDefault_resolvesDefaultColor() {
        val deferred = DeferredColor.Attribute(R.attr.titleTextColor)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#987654"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolve_withUnknownAttribute_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)

        // Default-theme context does not have <colorPrimary> attribute:
        deferred.resolve(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolve_withWrongAttributeType_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.isLightTheme)

        deferred.resolve(AppCompatContext())
    }
    //endregion
}
