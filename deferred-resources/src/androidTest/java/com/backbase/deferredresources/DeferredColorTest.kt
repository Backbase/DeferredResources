package com.backbase.deferredresources

import android.graphics.Color
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredColorTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredColor.Constant(Color.MAGENTA)
        assertThat(deferred.resolve(context)).isEqualTo(Color.MAGENTA)
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredColor.Resource(R.color.blue)
        assertThat(deferred.resolve(context)).isEqualTo(Color.BLUE)
    }

    @Test fun attribute_resolvesWithContext() {
        context.setTheme(R.style.Theme_AppCompat)
        val deferred = DeferredColor.Attribute(R.attr.colorPrimary)
        assertThat(deferred.resolve(context)).isEqualTo(Color.parseColor("#212121"))
    }
}
