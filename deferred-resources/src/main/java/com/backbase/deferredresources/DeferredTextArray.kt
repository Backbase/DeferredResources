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
import androidx.annotation.ArrayRes
import com.backbase.deferredresources.DeferredTextArray.Resource.Type
import com.backbase.deferredresources.text.ParcelableDeferredTextArray
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving a text array on demand.
 */
public interface DeferredTextArray {

    /**
     * Resolve the text array.
     */
    public fun resolve(context: Context): Array<out CharSequence>

    /**
     * A wrapper for a constant array of text [values].
     *
     * This class protects against array mutability by holding a copy of the input [values] and by always returning a
     * new copy of those [values] when resolved.
     */
    // Primary constructor is internal rather than private so the generated Creator can access it
    @Parcelize
    @Poko public class Constant internal constructor(
        private val values: List<CharSequence>
    ) : ParcelableDeferredTextArray {

        /**
         * Initialize with the given text [values].
         *
         * The given [values] array is copied on construction, so later external changes to the original will not be
         * reflected in this [DeferredTextArray].
         */
        public constructor(vararg values: CharSequence) : this(values.toList())

        /**
         * Convenience for initializing with a [Collection] of text [values].
         */
        public constructor(values: Collection<CharSequence>) : this(values.toList())

        /**
         * Always resolves to a new array copied from [values]. Changes to the returned array will not be reflected in
         * future calls to resolve this [DeferredTextArray].
         */
        override fun resolve(context: Context): Array<out CharSequence> = values.toTypedArray()
    }

    /**
     * A wrapper for a text [ArrayRes] [id]. Optionally set [type] to [Type.TEXT] to retain style information in each
     * resource.
     */
    @Parcelize
    @Poko public class Resource @JvmOverloads constructor(
        @ArrayRes private val id: Int,
        private val type: Type = Type.STRING
    ) : ParcelableDeferredTextArray {
        /**
         * Resolve [id] to a text array with the given [context].
         *
         * If [type] is [Type.STRING], resolves an array of (un-styled) strings. If [type] is [Type.TEXT], resolves an
         * array of styled [CharSequence]s.
         *
         * @see android.content.res.Resources.getStringArray
         * @see android.content.res.Resources.getTextArray
         */
        override fun resolve(context: Context): Array<out CharSequence> = when (type) {
            Type.STRING -> context.resources.getStringArray(id)
            Type.TEXT -> context.resources.getTextArray(id)
        }

        /**
         * The type of text resource to resolve.
         */
        public enum class Type {
            STRING, TEXT
        }
    }


}
