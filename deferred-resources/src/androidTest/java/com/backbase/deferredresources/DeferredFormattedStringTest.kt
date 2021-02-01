package com.backbase.deferredresources

import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.safeargs.sendAndReceiveWithSafeArgs
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class DeferredFormattedStringTest {

    @get:Rule val parcelableTester = ParcelableTester()

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredFormattedString.Constant("This is %s text.")
        assertThat(deferred.resolve(context, "static")).isEqualTo("This is static text.")
    }

    @Test fun constant_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedString>(
            DeferredFormattedString.Constant("Parcelable %d")
        )
    }

    @Test fun constant_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = { DeferredFormattedString.Constant("Safe args %d") },
        send = { send(it) },
        receive = { getDeferredFormattedStringArg() },
    )

    @Test fun resource_resolvesAndFormatsStringWithContext() {
        val deferred = DeferredFormattedString.Resource(R.string.formattedString)
        assertThat(deferred.resolve(context, "localized")).isEqualTo("This is localized text.")
    }

    @Test fun resource_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredFormattedString>(
            DeferredFormattedString.Resource(R.string.formattedString)
        )
    }

    @Test fun resource_sendAndReceiveWithSafeArgs() = sendAndReceiveWithSafeArgs(
        construct = { DeferredFormattedString.Resource(R.string.formattedString) },
        send = { send(it) },
        receive = { getDeferredFormattedStringArg() },
    )
}
