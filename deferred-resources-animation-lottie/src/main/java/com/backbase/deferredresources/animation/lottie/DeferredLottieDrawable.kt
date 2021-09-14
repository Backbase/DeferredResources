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
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.backbase.deferredresources.DeferredDrawable
import java.io.InputStream

public interface DeferredLottieDrawable : DeferredDrawable {

    override fun resolve(context: Context): LottieDrawable?

    public class Constant(
        private val drawable: LottieDrawable?,
    ) : DeferredLottieDrawable {
        override fun resolve(context: Context): LottieDrawable? = drawable
    }

    public class Asset(
        private val fileName: String,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {
        override fun resolve(context: Context): LottieDrawable? {
            val compositionResult = LottieCompositionFactory.fromAssetSync(context, fileName)
            when (val exception = compositionResult.exception) {
                null -> return compositionResult.value?.asDrawable()?.apply {
                    transformations(context)
                }
                else -> throw exception
            }
        }
    }

    public class Resource(
        @RawRes private val rawRes: Int,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {
        override fun resolve(context: Context): LottieDrawable? {
            val compositionResult = LottieCompositionFactory.fromRawResSync(context, rawRes)
            when (val exception = compositionResult.exception) {
                null -> return compositionResult.value?.asDrawable()?.apply {
                    transformations(context)
                }
                else -> throw exception
            }
        }
    }

    public class Stream(
        private val stream: InputStream,
        private val key: String? = null,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {
        override fun resolve(context: Context): LottieDrawable? {
            val compositionResult = LottieCompositionFactory.fromJsonInputStreamSync(stream, key ?: toString())
            when (val exception = compositionResult.exception) {
                null -> return compositionResult.value?.asDrawable()?.apply {
                    transformations(context)
                }
                else -> throw exception
            }
        }
    }
}

private fun LottieComposition.asDrawable(): LottieDrawable = LottieDrawable().apply {
    composition = this@asDrawable
}
