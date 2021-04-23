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
import com.backbase.deferredresources.text.ParcelableDeferredTextArray
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredTextArrayTest {

    private val expectedStringArray = arrayOf("Bold one", "Regular one")

    @Test fun constant_returnsConstantValues() {
        val deferred = DeferredTextArray.Constant("Some text", "More text")
        assertThat(deferred.resolve(context).asList()).isEqualTo(listOf("Some text", "More text"))
    }

    @Test fun constant_initializedWithList_returnsConstantValues() {
        val originalList = mutableListOf("A")
        val deferred = DeferredTextArray.Constant(originalList as Collection<String>)
        originalList[0] = "Z"

        assertThat(deferred.resolve(context).asList()).isEqualTo(listOf("A"))
    }

    @Test fun constant_equals_basedOnContents() {
        val deferredA1 = DeferredTextArray.Constant("A")
        val deferredA2 = DeferredTextArray.Constant("A")
        val deferredAB = DeferredTextArray.Constant("A", "B")

        assertThat(deferredA1).isEqualTo(deferredA2)
        assertThat(deferredA1.hashCode()).isEqualTo(deferredA2.hashCode())
        assertThat(deferredA1).isNotEqualTo(deferredAB)
        assertThat(deferredA1.hashCode()).isNotEqualTo(deferredAB.hashCode())

        assertThat(deferredA2).isEqualTo(deferredA1)
        assertThat(deferredA2.hashCode()).isEqualTo(deferredA1.hashCode())
        assertThat(deferredA2).isNotEqualTo(deferredAB)
        assertThat(deferredA2.hashCode()).isNotEqualTo(deferredAB.hashCode())

        assertThat(deferredAB).isNotEqualTo(deferredA1)
        assertThat(deferredAB.hashCode()).isNotEqualTo(deferredA1.hashCode())
        assertThat(deferredAB).isNotEqualTo(deferredA2)
        assertThat(deferredAB.hashCode()).isNotEqualTo(deferredA2.hashCode())
    }

    @Test fun constant_toString_includesContents() {
        val deferred = DeferredTextArray.Constant("Yes", "No")
        assertThat(deferred.toString()).isEqualTo("Constant(values=[Yes, No])")
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredTextArray>(DeferredTextArray.Constant("A", "B", "See", "D"))
    }

    @Test fun resource_withTypeString_resolvesStringsWithContext() {
        val deferred = DeferredTextArray.Resource(R.array.stringArray)

        val resolved = deferred.resolve(context)
        assertThat(resolved.asList()).isEqualTo(expectedStringArray.asList())
        resolved.forEach { item ->
            assertThat(item).isInstanceOf(String::class.java)
        }
    }

    @Test fun resource_withTypeText_resolvesTextWithContext() {
        val deferred = DeferredTextArray.Resource(R.array.stringArray, type = DeferredTextArray.Resource.Type.TEXT)

        val resolved = deferred.resolve(context)

        resolved.forEachIndexed { index, item ->
            assertThat(item.toString()).isEqualTo(expectedStringArray[index])
        }

        val boldItem = resolved[0]
        // Verify the resolved value is not a plain string:
        assertThat(boldItem).isInstanceOf(SpannedString::class.java)
        boldItem as SpannedString

        // Verify the resolved value has a single BOLD style span:
        val spans = boldItem.getSpans(0, boldItem.length, Object::class.java)
        assertThat(spans.size).isEqualTo(1)
        val span = spans[0]
        assertThat(span).isInstanceOf(StyleSpan::class.java)
        span as StyleSpan
        assertThat(span.style).isEqualTo(Typeface.BOLD)

        val regularItem = resolved[1]
        assertThat(regularItem).isInstanceOf(String::class.java)
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredTextArray>(DeferredTextArray.Resource(R.array.stringArray))
    }
}
