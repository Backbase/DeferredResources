package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.SpecificLocaleTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class QuantifiedDeferredTextTest : SpecificLocaleTest() {

    @Test fun withQuantity_producesInstanceEqualsToNormalConstructor() {
        val plurals = DeferredPlurals.Constant("Other")

        val withQuantity = plurals.withQuantity(9)
        val constructed = QuantifiedDeferredText(plurals, 9)
        assertThat(withQuantity).isEqualTo(constructed)
    }

    @Test fun quantified_resolvesBasedOnInitialQuantity() {
        setTestLanguage("en")

        val plurals = DeferredPlurals.Constant(one = "Zebra", other = "Zebras")

        val deferred0 = QuantifiedDeferredText(plurals, 0)
        assertThat(deferred0.resolve(context)).isEqualTo("Zebras")
        val deferred1 = QuantifiedDeferredText(plurals, 1)
        assertThat(deferred1.resolve(context)).isEqualTo("Zebra")
    }
}
