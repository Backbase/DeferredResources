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
import android.text.SpannedString
import android.text.style.StyleSpan
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.backbase.deferredresources.text.ParcelableDeferredText
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredTextTest {

    private val richTextWithoutTags = "Rich text"

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredText.Constant("Some text")
        assertThat(deferred.resolve(context)).isEqualTo("Some text")
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredText>(DeferredText.Constant("Parcelable"))
    }

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

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredText>(DeferredText.Resource(R.string.plainString))
    }
}
