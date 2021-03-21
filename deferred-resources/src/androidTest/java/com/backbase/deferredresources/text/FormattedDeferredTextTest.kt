package com.backbase.deferredresources.text

import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.backbase.deferredresources.DeferredFormattedString
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
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

    @Test fun formatted_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredText>(
            FormattedDeferredText(DeferredFormattedString.Constant("%s"), "Cool")
        )
    }

    @Test fun formatted_wrappingNonParcelable_throwsWhenMarshalled() {
        val wrapped = object : DeferredFormattedString {
            override fun resolve(context: Context, vararg formatArgs: Any): String = "${formatArgs.toList()}"
        }

        // Construction and resolution work normally:
        val formatted = FormattedDeferredText(wrapped, "Arg", "Another arg")
        assertThat(formatted.resolve(context)).isEqualTo("[Arg, Another arg]")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredText>(formatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $wrapped")
    }

    @Test fun formatted_withNonParcelableArg_throwsWhenMarshalled() {
        val wrapped = DeferredFormattedString.Constant("%d %s")
        val nonParcelableArg = ColorDrawable()

        // Construction and resolution work normally:
        val formatted = FormattedDeferredText(wrapped, -3, nonParcelableArg)
        assertThat(formatted.resolve(context)).isEqualTo("-3 $nonParcelableArg")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredText>(formatted)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelableArg")
    }
}
