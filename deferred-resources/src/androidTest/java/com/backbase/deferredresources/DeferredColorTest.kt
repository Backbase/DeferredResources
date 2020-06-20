package com.backbase.deferredresources

import android.graphics.Color
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredColorTest {

    @Test fun constant_withIntValue_returnsSameValue() {
        val deferred = DeferredColor.Constant(Color.MAGENTA)
        assertThat(deferred.resolve(context)).isEqualTo(Color.MAGENTA)
    }

    @Test fun constant_withStringValue_returnsParsedValue() {
        val deferred = DeferredColor.Constant("#00FF00")
        assertThat(deferred.resolve(context)).isEqualTo(Color.GREEN)
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredColor.Resource(R.color.blue)
        assertThat(deferred.resolve(context)).isEqualTo(Color.BLUE)
    }

    @Test fun attribute_resolvesWithContext() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)
        assertThat(deferred.resolve(AppCompatContext())).isEqualTo(Color.parseColor("#212121"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withUnknownAttribute_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)

        // Default-theme context does not have <colorPrimary> attribute:
        deferred.resolve(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withWrongAttributeType_throwsException() {
        val deferred = DeferredColor.Attribute(R.attr.isLightTheme)

        deferred.resolve(AppCompatContext())
    }
}
