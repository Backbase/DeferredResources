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
import androidx.annotation.StringRes
import com.backbase.deferredresources.internal.primaryLocale
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving a formatted string on demand.
 */
public interface DeferredFormattedString {

    /**
     * Resolve the string with the supplied [formatArgs].
     */
    public fun resolve(context: Context, vararg formatArgs: Any): String

    /**
     * A wrapper for a constant format-able [format] string.
     */
    @Parcelize
    @Poko public class Constant(
        private val format: String
    ) : ParcelableDeferredFormattedString {
        /**
         * Always resolves [format] with the supplied [formatArgs] and the primary locale from [context].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  =
            String.format(context.primaryLocale, format, *formatArgs)
    }

    /**
     * A wrapper for a format-able [StringRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @StringRes private val resId: Int
    ) : ParcelableDeferredFormattedString {

        /**
         * Resolve [resId] to a formatted string with the given [context] and [formatArgs].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  = context.getString(resId, *formatArgs)
    }
}
