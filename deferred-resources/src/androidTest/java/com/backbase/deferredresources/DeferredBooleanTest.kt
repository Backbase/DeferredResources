package com.backbase.deferredresources

import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredBooleanTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredBoolean.Constant(false)
        assertThat(deferred.resolve(context)).isEqualTo(false)
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredBoolean.Resource(R.bool.testBool)
        assertThat(deferred.resolve(context)).isEqualTo(true)
    }

    @Test fun attribute_resolvesWithContext() {
        val deferredDark = DeferredBoolean.Attribute(R.attr.isLightTheme)
        val isDark = deferredDark.resolve(AppCompatContext(light = false))
        assertThat(isDark).isEqualTo(false)

        val deferredLight = DeferredBoolean.Attribute(R.attr.isLightTheme)
        val isLight = deferredLight.resolve(AppCompatContext(light = true))
        assertThat(isLight).isEqualTo(true)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withUnknownAttribute_throwsException() {
        val deferred = DeferredBoolean.Attribute(R.attr.isLightTheme)

        // Default-theme context does not have <isLightTheme> attribute:
        deferred.resolve(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withWrongAttributeType_throwsException() {
        val deferred = DeferredBoolean.Attribute(R.attr.colorPrimary)

        deferred.resolve(AppCompatContext())
    }
}
