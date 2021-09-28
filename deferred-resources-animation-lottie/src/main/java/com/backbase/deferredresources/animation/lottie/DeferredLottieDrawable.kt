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
import android.graphics.drawable.Drawable
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.backbase.deferredresources.DeferredDrawable
import dev.drewhamilton.poko.Poko
import java.io.InputStream

/**
 * A wrapper for resolving a [LottieDrawable] on demand.
 */
public interface DeferredLottieDrawable : DeferredDrawable {

    /**
     * Resolve the [LottieDrawable].
     */
    override fun resolve(context: Context): LottieDrawable?

    /**
     * A wrapper for a constant LottieDrawable [drawable].
     */
    @Poko public class Constant(
        private val drawable: LottieDrawable?,
    ) : DeferredLottieDrawable {

        /**
         * Always resolves to [drawable], ignoring [context].
         */
        override fun resolve(context: Context): LottieDrawable? = drawable
    }

    /**
     * A wrapper for a [LottieDrawable] stored in assets folder with name [fileName].
     * Optionally provide [transformations] (such as [LottieDrawable.setRepeatCount]) to apply each time the
     * LottieDrawable is resolved.
     */
    @Poko public class Asset(
        private val fileName: String,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {

        /**
         * Resolve [fileName] to a [LottieDrawable] with the given [context].
         * Applies [transformations] before returning.
         */
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

    /**
     * A wrapper for a [LottieDrawable] [rawResId]. Optionally provide
     * [transformations] (such as [LottieDrawable.setRepeatCount]) to apply each time the LottieDrawable is resolved.
     */
    @Poko public class Resource(
        @RawRes private val rawResId: Int,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {

        /**
         * Resolve [rawResId] to a [LottieDrawable] with the given [context].
         * Applies [transformations] before returning.
         */
        override fun resolve(context: Context): LottieDrawable? {
            val compositionResult = LottieCompositionFactory.fromRawResSync(context, rawResId)
            when (val exception = compositionResult.exception) {
                null -> return compositionResult.value?.asDrawable()?.apply {
                    transformations(context)
                }
                else -> throw exception
            }
        }
    }

    /**
     * A wrapper for a [LottieDrawable] provided as a [stream] object from a remote or local storage location.
     * Optionally provide a cache [key] so that the stream can be cache-retrieved for consecutive [resolve] invocations.
     * Optionally provide [transformations] (such as [LottieDrawable.setRepeatCount]) to apply each time
     * the LottieDrawable is resolved.
     */
    @Poko public class Stream(
        private val stream: InputStream,
        private val key: String? = null,
        private val transformations: LottieDrawable.(Context) -> Unit = {},
    ) : DeferredLottieDrawable {

        /**
         * Resolve [stream] to a [LottieDrawable] with the given [context].
         * Applies [transformations] before returning.
         */
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
