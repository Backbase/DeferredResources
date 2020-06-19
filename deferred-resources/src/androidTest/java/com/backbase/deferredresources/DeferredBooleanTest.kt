package com.backbase.deferredresources

import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredBooleanTest {

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
        assertThat(deferredDark.resolve(AppCompatContext(light = false))).isEqualTo(false)

        val deferredLight = DeferredBoolean.Attribute(R.attr.isLightTheme)
        assertThat(deferredLight.resolve(AppCompatContext(light = true))).isEqualTo(true)
    }
}
