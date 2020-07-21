package com.backbase.deferredresources

import android.graphics.Color
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredColorTest {

    private val enabledState = intArrayOf(android.R.attr.state_enabled)
    private val disabledState = intArrayOf(-android.R.attr.state_enabled)

    //region Constant
    @Test fun constantResolve_withIntValue_returnsSameValue() {
        val deferred = DeferredColor.Constant(Color.MAGENTA)
        assertThat(deferred.resolve(context)).isEqualTo(Color.MAGENTA)
    }

    @Test fun constantResolve_withStringValue_returnsParsedValue() {
        val deferred = DeferredColor.Constant("#00FF00")
        assertThat(deferred.resolve(context)).isEqualTo(Color.GREEN)
    }

    @Test fun constantResolveToStateList_wrapsValue() {
        val deferred = DeferredColor.Constant(Color.MAGENTA)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isFalse()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.MAGENTA)
        assertThat(resolved.getColorForState(enabledState, Color.BLACK)).isEqualTo(Color.MAGENTA)
        assertThat(resolved.defaultColor).isEqualTo(Color.MAGENTA)
    }
    //endregion

    //region Resource
    @Test fun resourceResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Resource(R.color.blue)
        assertThat(deferred.resolve(context)).isEqualTo(Color.BLUE)
    }

    @Test fun resourceResolve_withSelectorColor_resolvesDefaultColor() {
        val deferred = DeferredColor.Resource(R.color.stateful_color)
        assertThat(deferred.resolve(context)).isEqualTo(Color.GREEN)
    }

    @Test fun resourceResolveToStateList_withStandardColor_resolvesStatelessList() {
        val deferred = DeferredColor.Resource(R.color.blue)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isFalse()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.BLUE)
        assertThat(resolved.getColorForState(enabledState, Color.BLACK)).isEqualTo(Color.BLUE)
        assertThat(resolved.defaultColor).isEqualTo(Color.BLUE)
    }

    @Test fun resourceResolveToStateList_withSelectorColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Resource(R.color.stateful_color)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#AAAAAA"))
        assertThat(resolved.getColorForState(enabledState, Color.BLACK)).isEqualTo(Color.GREEN)
        assertThat(resolved.defaultColor).isEqualTo(Color.GREEN)
    }
    //endregion

    //region Attribute
    @Test fun attributeResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#212121"))
    }

    @Test fun attributeResolve_withSelectorColor_resolvesColor() {
        val deferred = DeferredColor.Attribute(android.R.attr.textColorPrimary)
        assertThat(deferred.resolve(context)).isEqualTo(Color.parseColor("#de000000"))
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

    @Test fun attributeResolveToStateList_withSelectorColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Attribute(android.R.attr.textColorPrimary)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#42000000"))
        assertThat(resolved.getColorForState(enabledState, Color.BLACK)).isEqualTo(Color.parseColor("#de000000"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#de000000"))
    }

    @Test fun attributeResolveToStateList_withStandardColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)

        val resolved = deferred.resolveToStateList(AppCompatContext())
        assertThat(resolved.isStateful).isFalse()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#212121"))
        assertThat(resolved.getColorForState(enabledState, Color.BLACK)).isEqualTo(Color.parseColor("#212121"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#212121"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolveToStateList_withUnknownAttribute_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)

        // Default-theme context does not have <colorPrimary> attribute:
        deferred.resolveToStateList(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attributeResolveToStateList_withWrongAttributeType_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.isLightTheme)

        deferred.resolveToStateList(AppCompatContext())
    }
    //endregion
}
