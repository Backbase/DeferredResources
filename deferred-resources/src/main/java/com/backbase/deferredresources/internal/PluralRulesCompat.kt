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

package com.backbase.deferredresources.internal

import android.content.Context
import android.content.res.Resources
import android.icu.text.PluralRules
import android.os.Build
import androidx.annotation.RequiresApi
import com.backbase.deferredresources.R

internal sealed class PluralRulesCompat {

    /**
     * Given a [quantity], returns the keyword of the first rule that applies to the quantity.
     */
    abstract fun select(quantity: Int): String

    /**
     * Given a [number], returns the keyword of the first rule that applies to the number.
     *
     * Only supported on API 24+. On lower APIs, use [select(Int)][select].
     */
    @RequiresApi(24)
    open fun select(number: Double): String = throw UnsupportedOperationException("Only supported on API 24+")

    private class Legacy(
        private val resources: Resources
    ) : PluralRulesCompat() {
        override fun select(quantity: Int): String =
            resources.getQuantityString(R.plurals.deferredResources_internal_pluralKeyword, quantity)
    }

    @RequiresApi(24)
    private class Preferred(
        private val pluralRules: PluralRules
    ) : PluralRulesCompat() {
        override fun select(quantity: Int): String = select(quantity.toDouble())

        override fun select(number: Double): String = pluralRules.select(number)
    }

    companion object {
        const val KEYWORD_ZERO = "zero"
        const val KEYWORD_ONE = "one"
        const val KEYWORD_TWO = "two"
        const val KEYWORD_FEW = "few"
        const val KEYWORD_MANY = "many"
        const val KEYWORD_OTHER = "other"

        @JvmStatic fun forContext(context: Context) = if (Build.VERSION.SDK_INT < 24) {
            Legacy(context.resources)
        } else {
            Preferred(PluralRules.forLocale(context.primaryLocale))
        }

        @RequiresApi(24)
        @JvmStatic fun forContext(context: Context, type: PluralRules.PluralType) = if (Build.VERSION.SDK_INT < 24) {
            Legacy(context.resources)
        } else {
            Preferred(PluralRules.forLocale(context.primaryLocale, type))
        }
    }
}
