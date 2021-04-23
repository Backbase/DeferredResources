/*
 * Copyright 2020 Backbase R&D B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
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

    @Test fun quantified_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredText>(
            QuantifiedDeferredText(DeferredPlurals.Resource(R.plurals.plainPlurals), 99)
        )
    }

    @Test fun quantified_withNonParcelablePlurals_throwsWhenMarshalled() {
        val nonParcelablePlurals = object : DeferredPlurals {
            override fun resolve(context: Context, quantity: Int): String = "quantity $quantity"
        }

        // Construction and resolution work normally:
        val quantified = QuantifiedDeferredText(nonParcelablePlurals, 98)
        assertThat(quantified.resolve(context)).isEqualTo("quantity 98")

        // Only marshalling does not work:
        val exception = assertThrows(RuntimeException::class.java) {
            testParcelable<ParcelableDeferredText>(quantified)
        }
        assertThat(exception.message).isEqualTo("Parcel: unable to marshal value $nonParcelablePlurals")
    }
}
