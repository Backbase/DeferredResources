package com.backbase.deferredresources

import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredFormattedStringTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredFormattedString.Constant("This is %s text.")
        assertThat(deferred.resolve(context, "static")).isEqualTo("This is static text.")
    }

    @Test fun resource_resolvesAndFormatsStringWithContext() {
        val deferred = DeferredFormattedString.Resource(R.string.formattedString)
        assertThat(deferred.resolve(context, "localized")).isEqualTo("This is localized text.")
    }
}
