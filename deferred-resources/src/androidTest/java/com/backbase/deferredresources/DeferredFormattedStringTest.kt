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

package com.backbase.deferredresources

import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredFormattedStringTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredFormattedString.Constant("This is %s text.")
        assertThat(deferred.resolve(context, "static")).isEqualTo("This is static text.")
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredFormattedString>(
            DeferredFormattedString.Constant("Parcelable %d")
        )
    }

    @Test fun resource_resolvesAndFormatsStringWithContext() {
        val deferred = DeferredFormattedString.Resource(R.string.formattedString)
        assertThat(deferred.resolve(context, "localized")).isEqualTo("This is localized text.")
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredFormattedString>(
            DeferredFormattedString.Resource(R.string.formattedString)
        )
    }
}
