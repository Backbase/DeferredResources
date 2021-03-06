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
import android.icu.text.PluralRules
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import com.backbase.deferredresources.internal.primaryLocale
import com.backbase.deferredresources.text.ParcelableDeferredFormattedPlurals
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving format-able pluralized text on demand.
 */
public interface DeferredFormattedPlurals {

    /**
     * Resolve the string for the given [quantity] with the given [formatArgs].
     */
    public fun resolve(context: Context, quantity: Int, vararg formatArgs: Any = arrayOf(quantity)): String

    /**
     * A wrapper for constant format-able pluralized text.
     */
    // Primary constructor is internal rather than private so the generated Creator can access it
    @Parcelize
    @Poko public class Constant internal constructor(
        private val delegate: DeferredPlurals.Constant
    ) : ParcelableDeferredFormattedPlurals {

        /**
         * Constructor for API 24+. [type] can be supplied to choose between [PluralRules.PluralType.CARDINAL] and
         * [PluralRules.PluralType.ORDINAL]. If null, the system default of [PluralRules.PluralType.CARDINAL] is used
         * implicitly.
         */
        @RequiresApi(24)
        public constructor(
            other: String,
            zero: String = other,
            one: String = other,
            two: String = other,
            few: String = other,
            many: String = other,
            type: PluralRules.PluralType?
        ) : this(delegate = DeferredPlurals.Constant(other, zero, one, two, few, many, type))

        /**
         * Constructor for API < 24. "CARDINAL" plural type will be used implicitly.
         */
        public constructor(
            other: String,
            zero: String = other,
            one: String = other,
            two: String = other,
            few: String = other,
            many: String = other
        ) : this(delegate = DeferredPlurals.Constant(other, zero, one, two, few, many))

        /**
         * Resolves and formats one of the constructor-provided pluralized values depending on the primary locale
         * from [context], the given [quantity], and [formatArgs].
         */
        override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String {
            val unformatted = delegate.resolve(context, quantity).toString()
            return String.format(context.primaryLocale, unformatted, *formatArgs)
        }
    }

    /**
     * A wrapper for a format-able [PluralsRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @PluralsRes private val resId: Int
    ) : ParcelableDeferredFormattedPlurals {
        /**
         * Resolve [resId] to a formatted string with the given [context], [quantity], and [formatArgs].
         */
        override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String =
            context.resources.getQuantityString(resId, quantity, *formatArgs)
    }
}
