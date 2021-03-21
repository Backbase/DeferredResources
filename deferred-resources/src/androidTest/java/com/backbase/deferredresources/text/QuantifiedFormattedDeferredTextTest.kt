package com.backbase.deferredresources.text

import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
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

    @Test fun quantifiedAndFormatted_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredText>(
            QuantifiedFormattedDeferredText(DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals), 42)
        )
    }

    @Test fun quantifiedAndFormatted_withNonParcelablePlurals_throwsWhenMarshalled() {
        val nonParcelablePlurals = object : DeferredFormattedPlurals {
            override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String =
                "$quantity ${formatArgs.toList()}"
        }

        // Construction and resolution work normally:
        val quantifiedAndFormatted = QuantifiedFormattedDeferredText(nonParcelablePlurals, 1, "Arg")
        assertThat(quantifiedAndFormatted.resolve(context)).isEqualTo("1 [Arg]")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredText>(quantifiedAndFormatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelablePlurals")
    }

    @Test fun quantifiedAndFormatted_withNonParcelableArg_throwsWhenMarshalled() {
        val plurals = DeferredFormattedPlurals.Constant("%d %s")
        val nonParcelableArg = ColorDrawable()

        // Construction and resolution work normally:
        val quantifiedAndFormatted = QuantifiedFormattedDeferredText(plurals, 44, 44, nonParcelableArg)
        assertThat(quantifiedAndFormatted.resolve(context)).isEqualTo("44 $nonParcelableArg")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredText>(quantifiedAndFormatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelableArg")
    }
}
