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
import com.backbase.deferredresources.text.ParcelableDeferredText
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving text on demand.
 */
public interface DeferredText {

    /**
     * Resolve the text.
     */
    public fun resolve(context: Context): CharSequence

    /**
     * A wrapper for a constant text [value].
     */
    @Parcelize
    @Poko public class Constant(
        private val value: CharSequence
    ) : ParcelableDeferredText {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): CharSequence = value
    }

    /**
     * A wrapper for a text [resId].
     */
    @Parcelize
    @Poko public class Resource @JvmOverloads constructor(
        @StringRes private val resId: Int,
        private val type: Type = Type.STRING
    ) : ParcelableDeferredText {

        /**
         * Resolve [resId] to text with the given [context].
         *
         * If [type] is [Type.STRING], resolves a localized string. If [type] is [Type.TEXT], resolves a localized,
         * styled [CharSequence].
         *
         * @see android.content.Context.getString
         * @see android.content.Context.getText
         */
        override fun resolve(context: Context): CharSequence = when (type) {
            Type.STRING -> context.getString(resId)
            Type.TEXT -> context.getText(resId)
        }

        /**
         * The type of text resource to resolve.
         */
        public enum class Type {
            STRING, TEXT
        }
    }
}
