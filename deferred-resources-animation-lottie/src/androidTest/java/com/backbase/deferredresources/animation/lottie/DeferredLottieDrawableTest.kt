/*
 * Copyright 2021 Backbase R&D B.V.
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

package com.backbase.deferredresources.animation.lottie

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.lottie.LottieDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import com.backbase.deferredresources.animation.lottie.test.R
import java.io.InputStream

internal class DeferredLottieDrawableTest {

    @Test
    fun constant_returnsConstantValue() {
        val drawable = LottieDrawable()
        val deferred = DeferredLottieDrawable.Constant(drawable)
        assertThat(deferred.resolve(context)).isEqualTo(drawable)
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredLottieDrawable.Resource(R.raw.test_lottie_file)
        val resolved = deferred.resolve(context)

        assertThat(resolved).isInstanceOf(LottieDrawable::class.java)
    }

    @Test fun resource_withTransformations_resolvesWithContextAndAppliesTransformation() {
        val testRepeatCount = 4
        val deferred = DeferredLottieDrawable.Resource(R.raw.test_lottie_file) {
            repeatCount = testRepeatCount
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(LottieDrawable::class.java)
        resolved as LottieDrawable

        assertThat(resolved.repeatCount).isEqualTo(testRepeatCount)
    }

    @Test fun asset_resolvesWithContext() {
        val deferred = DeferredLottieDrawable.Asset("test_lottie_file.json")
        val resolved = deferred.resolve(context)

        assertThat(resolved).isInstanceOf(LottieDrawable::class.java)
    }

    @Test fun asset_withTransformations_resolvesWithContextAndAppliesTransformation() {
        val testRepeatCount = 4
        val deferred = DeferredLottieDrawable.Asset("test_lottie_file.json") {
            repeatCount = testRepeatCount
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(LottieDrawable::class.java)
        resolved as LottieDrawable

        assertThat(resolved.repeatCount).isEqualTo(testRepeatCount)
    }

    @Test fun stream_resolvesWithContext() {
        val deferred = DeferredLottieDrawable.Stream(inputStream("test_lottie_file.json"))

        assertThat(deferred.resolve(context)).isInstanceOf(LottieDrawable::class.java)
    }

    @Test fun stream_withTransformations_resolvesWithContextAndAppliesTransformation() {
        val testRepeatCount = 4
        val deferred = DeferredLottieDrawable.Stream(inputStream("test_lottie_file.json")) {
            repeatCount = testRepeatCount
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(LottieDrawable::class.java)
        resolved as LottieDrawable

        assertThat(resolved.repeatCount).isEqualTo(testRepeatCount)
    }

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context

    private fun inputStream(fileName: String): InputStream = context.assets.open(fileName)

}
