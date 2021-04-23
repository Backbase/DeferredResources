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

import com.backbase.deferredresources.bool.ParcelableDeferredBoolean
import com.backbase.deferredresources.test.AppCompatContext
import com.backbase.deferredresources.test.R
import com.backbase.deferredresources.test.context
import com.backbase.deferredresources.test.testParcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredBooleanTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredBoolean.Constant(false)
        assertThat(deferred.resolve(context)).isEqualTo(false)
    }

    @Test fun constant_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredBoolean>(DeferredBoolean.Constant(true))
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredBoolean.Resource(R.bool.testBool)
        assertThat(deferred.resolve(context)).isEqualTo(true)
    }

    @Test fun resource_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredBoolean>(DeferredBoolean.Resource(R.bool.testBool))
    }

    @Test fun attribute_resolvesWithContext() {
        val deferredDark = DeferredBoolean.Attribute(R.attr.isLightTheme)
        val isDark = deferredDark.resolve(AppCompatContext(light = false))
        assertThat(isDark).isEqualTo(false)

        val deferredLight = DeferredBoolean.Attribute(R.attr.isLightTheme)
        val isLight = deferredLight.resolve(AppCompatContext(light = true))
        assertThat(isLight).isEqualTo(true)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withUnknownAttribute_throwsException() {
        val deferred = DeferredBoolean.Attribute(R.attr.isLightTheme)

        // Default-theme context does not have <isLightTheme> attribute:
        deferred.resolve(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun attribute_withWrongAttributeType_throwsException() {
        val deferred = DeferredBoolean.Attribute(R.attr.colorPrimary)

        deferred.resolve(AppCompatContext())
    }

    @Test fun attribute_parcelsThroughBundle() {
        testParcelable<ParcelableDeferredBoolean>(DeferredBoolean.Attribute(R.attr.isLightTheme))
    }
}
