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

import android.graphics.Typeface
import android.icu.text.PluralRules
import android.text.SpannedString
import android.text.style.StyleSpan
import androidx.test.filters.SdkSuppress
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.SpecificLocaleTest
import com.backbase.deferredresources.test.testParcelable
import com.backbase.deferredresources.text.ParcelableDeferredPlurals
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredPluralsTest : SpecificLocaleTest() {

    @Test fun constant_defaultTypeAndUsLocale_resolvesOneAndOther() {
        setTestLanguage("en-US")

        val some = "Some"
        val deferred = DeferredPlurals.Constant(
            zero = "No",
            one = "A single",
            two = "A couple",
            few = "Not that many",
            many = "A bunch of",
            other = some
        )

        assertThat(deferred.resolve(context, 0)).isEqualTo(some)
        assertThat(deferred.resolve(context, 1)).isEqualTo("A single")
        assertThat(deferred.resolve(context, 2)).isEqualTo(some)
        assertThat(deferred.resolve(context, 3)).isEqualTo(some)
        assertThat(deferred.resolve(context, 14)).isEqualTo(some)
        assertThat(deferred.resolve(context, 100)).isEqualTo(some)
    }

    @SdkSuppress(minSdkVersion = 24)
    @Test fun constant_ordinalTypeAndUsLocale_resolvesOneTwoFewAndOther() {
        setTestLanguage("en-US")

        val some = "Some"
        val deferred = DeferredPlurals.Constant(
            zero = "No",
            one = "A single",
            two = "A couple",
            few = "Not that many",
            many = "A bunch of",
            other = some,
            type = PluralRules.PluralType.ORDINAL
        )
        assertThat(deferred.resolve(context, 0)).isEqualTo(some)
        assertThat(deferred.resolve(context, 1)).isEqualTo("A single")
        assertThat(deferred.resolve(context, 2)).isEqualTo("A couple")
        assertThat(deferred.resolve(context, 3)).isEqualTo("Not that many")
        assertThat(deferred.resolve(context, 14)).isEqualTo(some)
        assertThat(deferred.resolve(context, 100)).isEqualTo(some)
    }

    @Test fun constant_defaultTypeAndArabicLocale_resolvesAllPlurals() {
        setTestLanguage("ar")

        val deferred = DeferredPlurals.Constant(
            zero = "No",
            one = "A single",
            two = "A couple",
            few = "Not that many",
            many = "A bunch of",
            other = "Some"
        )
        assertThat(deferred.resolve(context, 0)).isEqualTo("No")
        assertThat(deferred.resolve(context, 1)).isEqualTo("A single")
        assertThat(deferred.resolve(context, 2)).isEqualTo("A couple")
        assertThat(deferred.resolve(context, 3)).isEqualTo("Not that many")
        assertThat(deferred.resolve(context, 14)).isEqualTo("A bunch of")
        assertThat(deferred.resolve(context, 100)).isEqualTo("Some")
    }

    @Test fun constant_defaultType_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredPlurals>(
            DeferredPlurals.Constant(
                zero = "No",
                one = "A single",
                two = "A couple",
                few = "Not that many",
                many = "A bunch of",
                other = "Some"
            )
        )
    }

    @SdkSuppress(minSdkVersion = 24)
    @Test fun constant_ordinalType_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredPlurals>(
            DeferredPlurals.Constant(
                zero = "No",
                one = "A single",
                two = "A couple",
                few = "Not that many",
                many = "A bunch of",
                other = "Some",
                type = PluralRules.PluralType.ORDINAL
            )
        )
    }

    @Test fun resource_withTypeString_resolvesStringWithContext() {
        setTestLanguage("en-US")

        val deferred = DeferredPlurals.Resource(R.plurals.plainPlurals)
        assertThat(deferred.resolve(context, 1)).isEqualTo("Car")
        assertThat(deferred.resolve(context, 9)).isEqualTo("Cars")
    }

    @Test fun resource_withTypeText_resolvesTextWithContext() {
        setTestLanguage("en-US")

        val deferred = DeferredPlurals.Resource(R.plurals.richPlurals, type = DeferredPlurals.Resource.Type.TEXT)

        assertThat(deferred.resolve(context, 1).toString()).isEqualTo("Limousine")
        val resolved4 = deferred.resolve(context, 4)
        assertThat(resolved4.toString()).isEqualTo("Limousines")
        // Verify the resolved value is not a plain string:
        assertThat(resolved4).isInstanceOf(SpannedString::class.java)
        resolved4 as SpannedString

        // Verify the resolved value has a single BOLD style span:
        val spans = resolved4.getSpans(0, resolved4.length, Object::class.java)
        assertThat(spans.size).isEqualTo(1)
        val span = spans[0]
        assertThat(span).isInstanceOf(StyleSpan::class.java)
        span as StyleSpan
        assertThat(span.style).isEqualTo(Typeface.ITALIC)
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredPlurals>(DeferredPlurals.Resource(R.plurals.plainPlurals))
    }
}
