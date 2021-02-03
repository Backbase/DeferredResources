package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

internal class QuantifiedDeferredFormattedStringTest : SpecificLocaleTest() {

    @get:Rule val parcelableTester = ParcelableTester()

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

    @Test fun quantified_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedString>(
            QuantifiedDeferredFormattedString(DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals), 7)
        )
    }

    @Test fun quantified_withNonParcelablePlurals_throwsWhenMarshalled() {
        val nonParcelablePlurals = object : DeferredFormattedPlurals {
            override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String =
                "$quantity ${formatArgs.toList()}"
        }

        // Construction and resolution work normally:
        val quantified = QuantifiedDeferredFormattedString(nonParcelablePlurals, 9)
        assertThat(quantified.resolve(context, "Arg")).isEqualTo("9 [Arg]")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedString>(quantified)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelablePlurals")
    }
}
