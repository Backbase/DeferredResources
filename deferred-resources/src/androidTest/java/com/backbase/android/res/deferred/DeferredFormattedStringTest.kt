package com.backbase.android.res.deferred

import com.backbase.android.res.deferred.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredFormattedStringTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredFormattedString.Constant("This is %s text.")
        assertThat(deferred.resolve(context, "static")).isEqualTo("This is static text.")
    }

    @Test fun resource_resolvesAndFormatsStringWithContext() {
        val deferred = DeferredFormattedString.Resource(R.string.formattedString)
        assertThat(deferred.resolve(context, "localized")).isEqualTo("This is localized text.")
    }
}
