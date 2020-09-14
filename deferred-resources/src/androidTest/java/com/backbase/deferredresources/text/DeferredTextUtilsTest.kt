package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.context
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredTextUtilsTest {

    @Test fun resolveToString_returnsCharSequenceToString() {
        val deferred = DeferredText.Constant(FakeCharSequence("Test string"))

        assertThat(deferred.resolveToString(context)).isEqualTo(FakeCharSequence.TO_STRING)
    }

    private class FakeCharSequence(
        private val wrapped: CharSequence
    ) : CharSequence by wrapped {
        override fun toString() = TO_STRING

        companion object {
            const val TO_STRING = "FakeCharSequence.toString"
        }
    }
}
