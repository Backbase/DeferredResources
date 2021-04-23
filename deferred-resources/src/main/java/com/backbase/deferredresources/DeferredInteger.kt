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
import androidx.annotation.IntegerRes
import com.backbase.deferredresources.integer.ParcelableDeferredInteger
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving an integer on demand.
 */
public interface DeferredInteger {

    /**
     * Resolve the integer.
     */
    public fun resolve(context: Context): Int

    /**
     * A wrapper for a constant integer [value].
     */
    @Parcelize
    @Poko public class Constant(
        private val value: Int
    ) : ParcelableDeferredInteger {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Int = value
    }

    /**
     * A wrapper for a [IntegerRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @IntegerRes private val resId: Int
    ) : ParcelableDeferredInteger {
        /**
         * Resolve [resId] to an integer with the given [context].
         */
        override fun resolve(context: Context): Int = context.resources.getInteger(resId)
    }
}
