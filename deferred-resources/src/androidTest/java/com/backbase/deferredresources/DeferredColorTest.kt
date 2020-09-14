package com.backbase.deferredresources

import android.graphics.Color
import androidx.test.filters.SdkSuppress
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredColorTest {

    private val disabledState = intArrayOf(-android.R.attr.state_enabled)
    private val checkedState = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
    private val defaultState = intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked)

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
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.MAGENTA)
        assertThat(resolved.defaultColor).isEqualTo(Color.MAGENTA)
    }
    //endregion

    //region Resource
    @Test fun resourceResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Resource(R.color.blue)
        assertThat(deferred.resolve(context)).isEqualTo(Color.BLUE)
    }

    @Test fun resourceResolve_withSelectorColor_resolvesDefaultColor() {
        val deferred = DeferredColor.Resource(R.color.stateful_color_without_attr)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#aaaaaa"))
    }

    @SdkSuppress(minSdkVersion = 23)
    @Test fun resourceResolve_withSelectorColorWithAttribute_resolvesDefaultColor() {
        val deferred = DeferredColor.Resource(R.color.stateful_color_with_attr)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#987654"))
    }

    @Test fun resourceResolveToStateList_withStandardColor_resolvesStatelessList() {
        val deferred = DeferredColor.Resource(R.color.blue)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isFalse()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.BLUE)
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.BLUE)
        assertThat(resolved.defaultColor).isEqualTo(Color.BLUE)
    }

    @Test fun resourceResolveToStateList_withSelectorColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Resource(R.color.stateful_color_without_attr)

        val resolved = deferred.resolveToStateList(context)
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#dbdbdb"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#aaaaaa"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#aaaaaa"))
    }

    @SdkSuppress(minSdkVersion = 23)
    @Test fun resourceResolveToStateList_withSelectorColorWithAttribute_resolvesExpectedStateList() {
        val deferred = DeferredColor.Resource(R.color.stateful_color_with_attr)

        val resolved = deferred.resolveToStateList(AppCompatContext())
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#dbdbdb"))
        assertThat(resolved.getColorForState(checkedState, Color.BLACK)).isEqualTo(Color.parseColor("#aaaaaa"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#987654"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#987654"))
    }
    //endregion

    //region Attribute
    @Test fun attributeResolve_withStandardColor_resolvesColor() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#987654"))
    }

    @Test fun attributeResolve_withSelectorColor_resolvesDefaultColor() {
        val deferred = DeferredColor.Attribute(R.attr.subtitleTextColor)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#aaaaaa"))
    }

    @SdkSuppress(minSdkVersion = 23)
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


    @Test fun attributeResolveToStateList_withSelectorColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Attribute(R.attr.subtitleTextColor)

        val resolved = deferred.resolveToStateList(AppCompatContext())
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#dbdbdb"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#aaaaaa"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#aaaaaa"))
    }

    @SdkSuppress(minSdkVersion = 23)
    @Test fun attributeResolveToStateList_withSelectorColorWithAttribute_resolvesExpectedStateList() {
        val deferred = DeferredColor.Attribute(R.attr.titleTextColor)

        val resolved = deferred.resolveToStateList(AppCompatContext())
        assertThat(resolved.isStateful).isTrue()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#dbdbdb"))
        assertThat(resolved.getColorForState(checkedState, Color.BLACK)).isEqualTo(Color.parseColor("#aaaaaa"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#987654"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#987654"))
    }

    @Test fun attributeResolveToStateList_withStandardColor_resolvesExpectedStateList() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)

        val resolved = deferred.resolveToStateList(AppCompatContext())
        assertThat(resolved.isStateful).isFalse()
        assertThat(resolved.getColorForState(disabledState, Color.BLACK)).isEqualTo(Color.parseColor("#987654"))
        assertThat(resolved.getColorForState(defaultState, Color.BLACK)).isEqualTo(Color.parseColor("#987654"))
        assertThat(resolved.defaultColor).isEqualTo(Color.parseColor("#987654"))
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
