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

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontsContractCompat.FontRequestCallback.FAIL_REASON_FONT_NOT_FOUND
import com.backbase.deferredresources.internal.orUiHandler
import dev.drewhamilton.poko.Poko

/**
 * A wrapper for resolving a typeface on demand.
 */
public interface DeferredTypeface {

    /**
     * Resolve the typeface synchronously.
     */
    public fun resolve(context: Context): Typeface?

    /**
     * Resolve the typeface asynchronously. [fontCallback] will be triggered on the [handler]'s thread. If [handler] is
     * null, [fontCallback] will be triggered on the UI thread.
     */
    public fun resolve(context: Context, fontCallback: ResourcesCompat.FontCallback, handler: Handler? = null)

    /**
     * A wrapper for a constant typeface [value].
     */
    @Poko public class Constant(
        private val value: Typeface
    ) : DeferredTypeface {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Typeface = value

        /**
         * Always post [value] to [fontCallback] on the [handler] thread, or the UI thread if [handler] is null.
         * [context] is ignored.
         */
        override fun resolve(context: Context, fontCallback: ResourcesCompat.FontCallback, handler: Handler?) {
            handler.orUiHandler().post { fontCallback.onFontRetrieved(value) }
        }
    }

    /**
     * A wrapper for a [FontRes] [id].
     */
    @Poko public class Resource(
        @FontRes private val id: Int
    ) : DeferredTypeface {
        /**
         * Resolve [id] to a typeface with the given [context].
         */
        override fun resolve(context: Context): Typeface? = ResourcesCompat.getFont(context, id)

        /**
         * Resolve [id] to a typeface asynchronously with the given [context]. [fontCallback] will be triggered on the
         * [handler]'s thread. If [handler] is null, [fontCallback] will be triggered on the UI thread.
         */
        override fun resolve(context: Context, fontCallback: ResourcesCompat.FontCallback, handler: Handler?): Unit =
            ResourcesCompat.getFont(context, id, fontCallback, handler)
    }

    @Poko public class Asset(
        private val path: String
    ) : DeferredTypeface {
        /**
         * Resolve the asset [path] to a typeface with the given [context]'s assets.
         */
        override fun resolve(context: Context): Typeface = Typeface.createFromAsset(context.assets, path)

        /**
         * Resolve the asset [path] to a typeface with the given [context]'s assets and provide it asynchronously to
         * [fontCallback]. If [path] is an invalid path, [FAIL_REASON_FONT_NOT_FOUND] is sent to the callback.
         */
        override fun resolve(context: Context, fontCallback: ResourcesCompat.FontCallback, handler: Handler?) {
            val value = try {
                resolve(context)
            } catch (@Suppress("Detekt.TooGenericExceptionCaught") runtimeException: RuntimeException) {
                // Generic RuntimeException is thrown by Typeface.createFromAsset. We use that to call
                //  `fontCallback.onFontRetrievalFailed(FAIL_REASON_FONT_NOT_FOUND)` below
                null
            }

            if (value == null)
                handler.orUiHandler().post { fontCallback.onFontRetrievalFailed(FAIL_REASON_FONT_NOT_FOUND) }
            else
                handler.orUiHandler().post { fontCallback.onFontRetrieved(value) }
        }
    }
}
