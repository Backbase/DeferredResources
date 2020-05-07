package com.backbase.android.res.deferred

import android.content.Context
import android.content.res.Resources
import android.icu.text.PluralRules
import android.os.Build
import androidx.annotation.RequiresApi

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
