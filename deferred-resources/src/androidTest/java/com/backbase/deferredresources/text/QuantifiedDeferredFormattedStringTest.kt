package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class QuantifiedDeferredFormattedStringTest : SpecificLocaleTest() {

    @Test fun withQuantity_producesInstanceEqualsToNormalConstructor() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("Other %s")

        val withQuantity = formattedPlurals.withQuantity(0)
        val constructed = QuantifiedDeferredFormattedString(formattedPlurals, 0)
        assertThat(withQuantity).isEqualTo(constructed)
    }

    @Test fun quantified_resolvesBasedOnInitialQuantity() {
        setTestLanguage("en")

        val formattedPlurals = DeferredFormattedPlurals.Constant(one = "%s pizza", other = "%s pizzas")

        val deferred = QuantifiedDeferredFormattedString(formattedPlurals, 2)
        assertThat(deferred.resolve(context, "Large")).isEqualTo("Large pizzas")
        assertThat(deferred.resolve(context, "Small")).isEqualTo("Small pizzas")
    }
}
