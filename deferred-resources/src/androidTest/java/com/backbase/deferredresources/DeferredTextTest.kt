package com.backbase.deferredresources

import android.graphics.Typeface
import android.text.SpannedString
import android.text.style.StyleSpan
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredTextTest : SpecificLocaleTest() {

    private val richTextWithoutTags = "Rich text"

    //region Constant
    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredText.Constant("Some text")
        assertThat(deferred.resolve(context)).isEqualTo("Some text")
    }
    //endregion

    //region Resource
    @Test fun resource_withTypeString_resolvesStringWithContext() {
        val deferred = DeferredText.Resource(R.string.richText)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isEqualTo(richTextWithoutTags)
        assertThat(resolved).isInstanceOf(String::class.java)
    }

    @Test fun resource_withTypeText_resolvesTextWithContext() {
        val deferred = DeferredText.Resource(R.string.richText, type = DeferredText.Resource.Type.TEXT)

        val resolved = deferred.resolve(context)

        assertThat(resolved.toString()).isEqualTo(richTextWithoutTags)

        // Verify the resolved value is not a plain string:
        assertThat(resolved).isInstanceOf(SpannedString::class.java)
        resolved as SpannedString

        // Verify the resolved value has a single BOLD style span:
        val spans = resolved.getSpans(0, resolved.length, Object::class.java)
        assertThat(spans.size).isEqualTo(1)
        val span = spans[0]
        assertThat(span).isInstanceOf(StyleSpan::class.java)
        span as StyleSpan
        assertThat(span.style).isEqualTo(Typeface.BOLD)
    }
    //endregion

    //region Quantified
    @Test fun quantified_resolvesBasedOnInitialQuantity() {
        setTestLanguage("en")

        val plurals = DeferredPlurals.Constant(one = "Zebra", other = "Zebras")

        val deferred0 = DeferredText.Quantified(plurals, 0)
        assertThat(deferred0.resolve(context)).isEqualTo("Zebras")
        val deferred1 = DeferredText.Quantified(plurals, 1)
        assertThat(deferred1.resolve(context)).isEqualTo("Zebra")
    }
    //endregion

    //region Formatted
    @Test fun formatted_resolvesBasedOnInitialFormatArgs() {
        val formattedString = DeferredFormattedString.Constant("%s and %s")

        val deferredA = DeferredText.Formatted(formattedString, "A", "B")
        assertThat(deferredA.resolve(context)).isEqualTo("A and B")
        val deferredX = DeferredText.Formatted(formattedString, "X", "Y")
        assertThat(deferredX.resolve(context)).isEqualTo("X and Y")
    }

    @Test fun formatted_equals_differentFormattedStrings_notEquals() {
        val deferredA = DeferredText.Formatted(DeferredFormattedString.Constant("A"))
        val deferredB = DeferredText.Formatted(DeferredFormattedString.Constant("B"))

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun formatted_equals_differentFormatArgs_notEquals() {
        val formattedString = DeferredFormattedString.Constant("%s")
        val deferredA = DeferredText.Formatted(formattedString, "A")
        val deferredB = DeferredText.Formatted(formattedString, "B")

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun formatted_equals_sameFormattedStringAndFormatArgs_equals() {
        val formattedString = DeferredFormattedString.Constant("%s")
        val deferredA = DeferredText.Formatted(formattedString, "100")
        val deferredB = DeferredText.Formatted(formattedString, "100")

        assertThat(deferredA).isEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isEqualTo(deferredB.hashCode())
    }

    @Test fun formatted_toString_includesFormattedStringAndFormatArgs() {
        val deferred = DeferredText.Formatted(DeferredFormattedString.Constant("%s"), "Yes")
        assertThat(deferred.toString())
            .isEqualTo("Formatted(deferredFormattedString=Constant(format=%s), formatArgs=[Yes])")
    }
    //endregion

    //region QuantifiedAndFormatted
    @Test fun quantifiedAndFormatted_resolvesBasedOnInitialFormatArgs() {
        setTestLanguage("en")

        val formattedPlurals = DeferredFormattedPlurals.Constant(one = "%d panda", other = "%d pandas")

        val deferredA = DeferredText.QuantifiedAndFormatted(formattedPlurals, 1)
        assertThat(deferredA.resolve(context)).isEqualTo("1 panda")
        val deferredX = DeferredText.QuantifiedAndFormatted(formattedPlurals, 2)
        assertThat(deferredX.resolve(context)).isEqualTo("2 pandas")
    }

    @Test fun quantifiedAndFormatted_equals_differentFormattedPlurals_notEquals() {
        val deferredA = DeferredText.QuantifiedAndFormatted(DeferredFormattedPlurals.Constant("A"), 0)
        val deferredB = DeferredText.QuantifiedAndFormatted(DeferredFormattedPlurals.Constant("B"), 0)

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_differentQuantities_notEquals() {
        val deferredA = DeferredText.QuantifiedAndFormatted(DeferredFormattedPlurals.Constant("A"), 0)
        val deferredB = DeferredText.QuantifiedAndFormatted(DeferredFormattedPlurals.Constant("A"), 1)

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_differentFormatArgs_notEquals() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferredA = DeferredText.QuantifiedAndFormatted(formattedPlurals, 45, "A")
        val deferredB = DeferredText.QuantifiedAndFormatted(formattedPlurals, 45, "B")

        assertThat(deferredA).isNotEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isNotEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_equals_sameFormattedPluralsAndQuantityAndFormatArgs_equals() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferredA = DeferredText.QuantifiedAndFormatted(formattedPlurals, 100, "100")
        val deferredB = DeferredText.QuantifiedAndFormatted(formattedPlurals, 100, "100")

        assertThat(deferredA).isEqualTo(deferredB)
        assertThat(deferredA.hashCode()).isEqualTo(deferredB.hashCode())
    }

    @Test fun quantifiedAndFormatted_toString_includesFormattedPluralsAndQuantityAndFormatArgs() {
        val formattedPlurals = DeferredFormattedPlurals.Constant("%s")
        val deferred = DeferredText.QuantifiedAndFormatted(formattedPlurals, 1, "Yes")
        assertThat(deferred.toString()).isEqualTo(
            "QuantifiedAndFormatted(deferredFormattedPlurals=$formattedPlurals, quantity=1, formatArgs=[Yes])"
        )
    }
    //endregion
}
