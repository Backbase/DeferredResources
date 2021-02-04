package com.backbase.deferredresources

import com.backbase.deferredresources.integer.ParcelableDeferredInteger
import com.backbase.deferredresources.test.ParcelableTester
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class DeferredIntegerTest {

    @get:Rule val parcelableTester = ParcelableTester()

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredInteger.Constant(8723)
        assertThat(deferred.resolve(context)).isEqualTo(8723)
    }

    @Test fun constant_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredInteger>(DeferredInteger.Constant(9872345))
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredInteger.Resource(R.integer.five)
        assertThat(deferred.resolve(context)).isEqualTo(5)
    }

    @Test fun resource_parcelsThroughBundle() {
        parcelableTester.testParcelableThroughBundle<ParcelableDeferredInteger>(
            DeferredInteger.Resource(R.integer.five)
        )
    }
}
