package com.backbase.deferredresources

import android.os.Build
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
        context.setTheme(R.style.Theme_AppCompat)
        val deferredDark = DeferredBoolean.Attribute(R.attr.isLightTheme)
        assertThat(deferredDark.resolve(context)).isEqualTo(false)

        context.setTheme(R.style.Theme_AppCompat_Light)
        val deferredLight = DeferredBoolean.Attribute(R.attr.isLightTheme)

        val expected = Build.VERSION.SDK_INT > 22
        assertThat(deferredLight.resolve(context)).isEqualTo(expected)
    }
}
