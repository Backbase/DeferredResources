package com.backbase.deferredresources

import android.icu.text.PluralRules
import androidx.test.filters.SdkSuppress
import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.backbase.deferredresources.test.safeargs.sendAndReceiveWithSafeArgs
import com.backbase.deferredresources.text.ParcelableDeferredFormattedPlurals
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class DeferredFormattedPluralsTest : SpecificLocaleTest() {

    @get:Rule val parcelableTester = ParcelableTester()

    @Test fun constant_defaultTypeAndUsLocale_resolvesOneAndOther() {
        setTestLanguage("en-US")

        val deferred = DeferredFormattedPlurals.Constant(
            zero = "No %s",
            one = "A single %s",
            two = "A couple %s",
            few = "Not that many %s",
            many = "A herd of %s",
            other = "Some %s"
        )
        val moose = "moose"
        val someMoose = "Some $moose"

        assertThat(deferred.resolve(context, 0, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 1, moose)).isEqualTo("A single $moose")
        assertThat(deferred.resolve(context, 2, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 3, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 14, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 100, moose)).isEqualTo(someMoose)
    }

    @SdkSuppress(minSdkVersion = 24)
    @Test fun constant_ordinalTypeAndUsLocale_resolvesOneTwoFewAndOther() {
        setTestLanguage("en-US")

        val deferred = DeferredFormattedPlurals.Constant(
            zero = "No %s",
            one = "A single %s",
            two = "A couple %s",
            few = "Not that many %s",
            many = "A herd of %s",
            other = "Some %s",
            type = PluralRules.PluralType.ORDINAL
        )
        val moose = "moose"
        val someMoose = "Some $moose"
        assertThat(deferred.resolve(context, 0, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 1, moose)).isEqualTo("A single $moose")
        assertThat(deferred.resolve(context, 2, moose)).isEqualTo("A couple $moose")
        assertThat(deferred.resolve(context, 3, moose)).isEqualTo("Not that many $moose")
        assertThat(deferred.resolve(context, 14, moose)).isEqualTo(someMoose)
        assertThat(deferred.resolve(context, 100, moose)).isEqualTo(someMoose)
    }

    @Test fun constant_defaultTypeAndArabicLocale_resolvesAllPlurals() {
        setTestLanguage("ar")

        val deferred = DeferredFormattedPlurals.Constant(
            zero = "No %s",
            one = "A single %s",
            two = "A couple %s",
            few = "Not that many %s",
            many = "A herd of %s",
            other = "Some %s"
        )
        val moose = "moose"
        assertThat(deferred.resolve(context, 0, moose)).isEqualTo("No $moose")
        assertThat(deferred.resolve(context, 1, moose)).isEqualTo("A single $moose")
        assertThat(deferred.resolve(context, 2, moose)).isEqualTo("A couple $moose")
        assertThat(deferred.resolve(context, 3, moose)).isEqualTo("Not that many $moose")
        assertThat(deferred.resolve(context, 14, moose)).isEqualTo("A herd of $moose")
        assertThat(deferred.resolve(context, 100, moose)).isEqualTo("Some $moose")
    }

    @Test fun constant_defaultType_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedPlurals>(
            DeferredFormattedPlurals.Constant(
                zero = "No %s",
                one = "A single %s",
                two = "A couple %s",
                few = "Not that many %s",
                many = "A herd of %s",
                other = "Some %s",
            )
        )
    }

    @SdkSuppress(minSdkVersion = 24)
    @Test fun constant_ordinalType_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedPlurals>(
            DeferredFormattedPlurals.Constant(
                zero = "No %s",
                one = "A single %s",
                two = "A couple %s",
                few = "Not that many %s",
                many = "A herd of %s",
                other = "Some %s",
                type = PluralRules.PluralType.ORDINAL
            )
        )
    }

    @Test fun constant_defaultType_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = {
            DeferredFormattedPlurals.Constant(
                zero = "No %s",
                one = "A single %s",
                two = "A couple %s",
                few = "Not that many %s",
                many = "A herd of %s",
                other = "Some %s",
            )
        },
        send = { send(it) },
        receive = { getDeferredFormattedPlurals() },
    )

    @SdkSuppress(minSdkVersion = 24)
    @Test fun constant_ordinalType_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = {
            DeferredFormattedPlurals.Constant(
                zero = "No %s",
                one = "A single %s",
                two = "A couple %s",
                few = "Not that many %s",
                many = "A herd of %s",
                other = "Some %s",
                type = PluralRules.PluralType.ORDINAL
            )
        },
        send = { send(it) },
        receive = { getDeferredFormattedPlurals() },
    )

    @Test fun resource_resolvesAndFormatsStringWithContext() {
        setTestLanguage("en-US")

        val deferred = DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals)

        assertThat(deferred.resolve(context, 1)).isEqualTo("1 bear")
        assertThat(deferred.resolve(context, 9)).isEqualTo("9 bears")
    }

    @Test fun resource_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedPlurals>(
            DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals)
        )
    }

    @Test fun resource_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = { DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals) },
        send = { send(it) },
        receive = { getDeferredFormattedPlurals() },
    )
}
