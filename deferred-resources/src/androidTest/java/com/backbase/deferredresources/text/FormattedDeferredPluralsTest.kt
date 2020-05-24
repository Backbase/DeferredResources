package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.SpecificLocaleTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FormattedDeferredPluralsTest : SpecificLocaleTest() {

    @Test fun withFormatArgs_producesInstanceEqualsToNormalConstructor() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s and %s")

        val withFormatArgs = formattedPlurals.withFormatArgs("A", "B")
        val constructed = FormattedDeferredPlurals(formattedPlurals, "A", "B")
        assertThat(withFormatArgs).isEqualTo(constructed)
    }

    @Test fun resolve_formatsWithFormatArgs() {
        setTestLanguage("en")

        val formattedString = DeferredFormattedPlurals.Constant(one = "%s scone", other = "%s scones")

        val deferred = FormattedDeferredPlurals(formattedString, "Tasty")
        assertThat(deferred.resolve(context, 1)).isEqualTo("Tasty scone")
        assertThat(deferred.resolve(context, 10)).isEqualTo("Tasty scones")
    }

    @Test fun equals_differentFormattedPlurals_notEquals() {
        val deferredA = FormattedDeferredPlurals(DeferredFormattedPlurals.Constant("A"))
        val deferredB = FormattedDeferredPlurals(DeferredFormattedPlurals.Constant("B"))

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun equals_differentFormatArgs_notEquals() {
        val formattedString = DeferredFormattedPlurals.Constant("%s")
        val deferredA = FormattedDeferredPlurals(formattedString, "A")
        val deferredB = FormattedDeferredPlurals(formattedString, "B")

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun equals_sameFormattedPluralsAndFormatArgs_equals() {
        val formattedString = DeferredFormattedPlurals.Constant("%s")
        val deferredA = FormattedDeferredPlurals(formattedString, "100")
        val deferredB = FormattedDeferredPlurals(formattedString, "100")

        assertThat(deferredA).isEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isEqualTo(deferredB.hashCode())
    }

    @Test fun toString_includesFormattedStringAndFormatArgs() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferred = FormattedDeferredPlurals(formattedPlurals, "Yes")
        assertThat(deferred.toString())
            .isEqualTo("FormattedDeferredPlurals(wrapped=$formattedPlurals, formatArgs=[Yes])")
    }
}
