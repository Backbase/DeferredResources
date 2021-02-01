package com.backbase.deferredresources.text

import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.backbase.deferredresources.test.safeargs.sendAndReceiveWithSafeArgs
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

internal class FormattedDeferredPluralsTest : SpecificLocaleTest() {

    @get:Rule val parcelableTester = ParcelableTester()

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

    @Test fun formatted_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredPlurals>(
            FormattedDeferredPlurals(DeferredFormattedPlurals.Constant("%s"), "Cool")
        )
    }

    @Test fun formatted_withNonParcelablePlurals_throwsWhenMarshalled() {
        val nonParcelablePlurals = object : DeferredFormattedPlurals {
            override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String =
                "$quantity ${formatArgs.toList()}"
        }

        // Construction and resolution work normally:
        val formatted = FormattedDeferredPlurals(nonParcelablePlurals, "Arg")
        assertThat(formatted.resolve(context, 19)).isEqualTo("19 [Arg]")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            parcelableTester.testParcelableThroughBundle<ParcelableDeferredPlurals>(formatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelablePlurals")
    }

    @Test fun formatted_withNonParcelableArg_throwsWhenMarshalled() {
        val plurals = DeferredFormattedPlurals.Constant("%d %s")
        val nonParcelableArg = ColorDrawable()

        // Construction and resolution work normally:
        val formatted = FormattedDeferredPlurals(plurals, 44, nonParcelableArg)
        assertThat(formatted.resolve(context, 19)).isEqualTo("44 $nonParcelableArg")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            parcelableTester.testParcelableThroughBundle<ParcelableDeferredPlurals>(formatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelableArg")
    }

    @Test fun formatted_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = {
            FormattedDeferredPlurals(DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals), 0)
        },
        send = { send(it) },
        receive = { getDeferredPluralsArg() },
    )
}
