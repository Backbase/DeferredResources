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

import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.test.context
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredTextUtilsTest {

    @Test fun resolveToString_returnsCharSequenceToString() {
        val deferred = DeferredText.Constant(FakeCharSequence("Test string"))

        assertThat(deferred.resolveToString(context)).isEqualTo(FakeCharSequence.TO_STRING)
    }

    private class FakeCharSequence(
        private val wrapped: CharSequence
    ) : CharSequence by wrapped {
        override fun toString() = TO_STRING

        companion object {
            const val TO_STRING = "FakeCharSequence.toString"
        }
    }
}
