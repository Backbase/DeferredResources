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

import com.backbase.deferredresources.integer.ParcelableDeferredIntegerArray
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredIntegerArrayTest {

    private val expectedIntArray = intArrayOf(101, 103)

    @Test fun constant_returnsConstantValues() {
        val deferred = DeferredIntegerArray.Constant(9, 8)
        assertThat(deferred.resolve(context)).asList().isEqualTo(listOf(9, 8))
    }

    @Test fun constant_initializedWithList_returnsConstantValues() {
        val originalList = mutableListOf(1)
        val deferred = DeferredIntegerArray.Constant(originalList as Collection<Int>)
        originalList[0] = 99

        assertThat(deferred.resolve(context)).asList().isEqualTo(listOf(1))
    }

    @Test fun constant_arrayChangedAfterResolved_doesNotAffectRepeatedResolutions() {
        val deferred: DeferredIntegerArray = DeferredIntegerArray.Constant(1)

        val resolved1 = deferred.resolve(context)
        resolved1[0] = 2

        val resolved2 = deferred.resolve(context)
        assertThat(resolved2).asList().isEqualTo(listOf(1))
    }

    @Test fun constant_equals_basedOnContents() {
        val deferredA1 = DeferredIntegerArray.Constant(1)
        val deferredA2 = DeferredIntegerArray.Constant(1)
        val deferredAB = DeferredIntegerArray.Constant(1, 2)

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
        val deferred = DeferredIntegerArray.Constant(1, 0)
        assertThat(deferred.toString()).isEqualTo("Constant(values=[1, 0])")
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredIntegerArray>(DeferredIntegerArray.Constant(1, 1, 2, 3, 5, 8))
    }

    @Test fun resource_resolvesIntegersWithContext() {
        val deferred = DeferredIntegerArray.Resource(R.array.integerArray)

        val resolved = deferred.resolve(context)
        assertThat(resolved).asList().isEqualTo(expectedIntArray.asList())
    }

    @Test fun resource_arrayChangedAfterResolved_doesNotAffectRepeatedResolutions() {
        val deferred: DeferredIntegerArray = DeferredIntegerArray.Resource(R.array.integerArray)

        val resolved1 = deferred.resolve(context)
        resolved1[0] = 102

        val resolved2 = deferred.resolve(context)
        assertThat(resolved2[0]).isEqualTo(101)
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredIntegerArray>(DeferredIntegerArray.Resource(R.array.integerArray))
    }
}
