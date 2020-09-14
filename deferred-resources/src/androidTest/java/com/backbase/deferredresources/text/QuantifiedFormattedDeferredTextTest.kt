package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.SpecificLocaleTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class QuantifiedFormattedDeferredTextTest : SpecificLocaleTest() {

    @Test fun withQuantityAndFormatArgs_producesInstanceEqualsToNormalConstructor() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%d things")

        val withQuantityFormatArgs = formattedPlurals.withQuantityAndFormatArgs(4)
        val constructed = QuantifiedFormattedDeferredText(formattedPlurals, 4)
        assertThat(withQuantityFormatArgs).isEqualTo(constructed)
    }

    @Test fun quantifiedAndFormatted_resolvesBasedOnInitialFormatArgs() {
        setTestLanguage("en")

        val formattedPlurals = DeferredFormattedPlurals.Constant(one = "%d panda", other = "%d pandas")

        val deferredA = QuantifiedFormattedDeferredText(formattedPlurals, 1)
        assertThat(deferredA.resolve(context)).isEqualTo("1 panda")
        val deferredX = QuantifiedFormattedDeferredText(formattedPlurals, 2)
        assertThat(deferredX.resolve(context)).isEqualTo("2 pandas")
    }

    @Test fun quantifiedAndFormatted_equals_differentFormattedPlurals_notEquals() {
        val deferredA = QuantifiedFormattedDeferredText(DeferredFormattedPlurals.Constant("A"), 0)
        val deferredB = QuantifiedFormattedDeferredText(DeferredFormattedPlurals.Constant("B"), 0)

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_differentQuantities_notEquals() {
        val deferredA = QuantifiedFormattedDeferredText(DeferredFormattedPlurals.Constant("A"), 0)
        val deferredB = QuantifiedFormattedDeferredText(DeferredFormattedPlurals.Constant("A"), 1)

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_differentFormatArgs_notEquals() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferredA = QuantifiedFormattedDeferredText(formattedPlurals, 45, "A")
        val deferredB = QuantifiedFormattedDeferredText(formattedPlurals, 45, "B")

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_sameFormattedPluralsAndQuantityAndFormatArgs_equals() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferredA = QuantifiedFormattedDeferredText(formattedPlurals, 100, "100")
        val deferredB = QuantifiedFormattedDeferredText(formattedPlurals, 100, "100")

        assertThat(deferredA).isEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_toString_includesFormattedPluralsAndQuantityAndFormatArgs() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferred = QuantifiedFormattedDeferredText(formattedPlurals, 1, "Yes")
        assertThat(deferred.toString()).isEqualTo(
            "QuantifiedFormattedDeferredText(wrapped=$formattedPlurals, quantity=1, formatArgs=[Yes])"
        )
    }
    //endregion
}
