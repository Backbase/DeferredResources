package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredFormattedString
import com.backbase.deferredresources.test.context
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class FormattedDeferredTextTest {

    @Test fun withFormatArgs_producesInstanceEqualsToNormalConstructor() {
        val formattedString = DeferredFormattedString.Constant("%s and %s")

        val withFormatArgs = formattedString.withFormatArgs("A", "B")
        val constructed = FormattedDeferredText(formattedString, "A", "B")
        assertThat(withFormatArgs).isEqualTo(constructed)
    }

    @Test fun resolve_formatsWithFormatArgs() {
        val formattedString = DeferredFormattedString.Constant("%s and %s")

        val deferredA = FormattedDeferredText(formattedString, "A", "B")
        assertThat(deferredA.resolve(context)).isEqualTo("A and B")
        val deferredX = FormattedDeferredText(formattedString, "X", "Y")
        assertThat(deferredX.resolve(context)).isEqualTo("X and Y")
    }

    @Test fun equals_differentFormattedStrings_notEquals() {
        val deferredA = FormattedDeferredText(DeferredFormattedString.Constant("A"))
        val deferredB = FormattedDeferredText(DeferredFormattedString.Constant("B"))

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun equals_differentFormatArgs_notEquals() {
        val formattedString = DeferredFormattedString.Constant("%s")
        val deferredA = FormattedDeferredText(formattedString, "A")
        val deferredB = FormattedDeferredText(formattedString, "B")

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun equals_sameFormattedStringAndFormatArgs_equals() {
        val formattedString = DeferredFormattedString.Constant("%s")
        val deferredA = FormattedDeferredText(formattedString, "100")
        val deferredB = FormattedDeferredText(formattedString, "100")

        assertThat(deferredA).isEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isEqualTo(deferredB.hashCode())
    }

    @Test fun toString_includesFormattedStringAndFormatArgs() {
        val deferred = FormattedDeferredText(DeferredFormattedString.Constant("%s"), "Yes")
        assertThat(deferred.toString())
            .isEqualTo("FormattedDeferredText(wrapped=Constant(format=%s), formatArgs=[Yes])")
    }
}
