package com.backbase.android.res.deferred

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.PluralRules
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving pluralized text on demand.
 */
interface DeferredPlurals {

    /**
     * Resolve the text for the given [quantity].
     */
    fun resolve(context: Context, quantity: Int): CharSequence

    /**
     * A wrapper for constant pluralized text. [zero], [one], [two], [few], and [many] are locale-specific,
     * quantity-specific text values, while [other] is the fallback.
     *
     * [type] can be supplied to choose between [PluralRules.PluralType.CARDINAL] and [PluralRules.PluralType.ORDINAL].
     * If null, the system default of [PluralRules.PluralType.CARDINAL] is used implicitly.
     */
    @DataApi class Constant @RequiresApi(24) constructor(
        private val other: CharSequence,
        private val zero: CharSequence = other,
        private val one: CharSequence = other,
        private val two: CharSequence = other,
        private val few: CharSequence = other,
        private val many: CharSequence = other,
        private val type: PluralRules.PluralType? = null
    ) : DeferredPlurals {

        /**
         * Constructor for APIs < 24. "CARDINAL" plural type will be used implicitly.
         */
        @SuppressLint("NewApi") // Safely calls API 24 constructor with null
        constructor(
            other: CharSequence,
            zero: CharSequence = other,
            one: CharSequence = other,
            two: CharSequence = other,
            few: CharSequence = other,
            many: CharSequence = other
        ) : this(other, zero, one, two, few, many, null)

        /**
         * Resolves to one of [zero], [one], [two], [few], [many], or [other] depending on the device's primary locale
         * and the given [quantity].
         */
        @SuppressLint("NewApi") // `type` is known to be safely null on API < 24
        override fun resolve(context: Context, quantity: Int): CharSequence {
            val pluralRules = if (type == null)
                PluralRulesCompat.forContext(context)
            else
                PluralRulesCompat.forContext(context, type)

            return when (pluralRules.select(quantity)) {
                PluralRulesCompat.KEYWORD_ZERO -> zero
                PluralRulesCompat.KEYWORD_ONE -> one
                PluralRulesCompat.KEYWORD_TWO -> two
                PluralRulesCompat.KEYWORD_FEW -> few
                PluralRulesCompat.KEYWORD_MANY -> many
                else -> other
            }
        }
    }

    /**
     * A wrapper for a [PluralsRes] [resId].
     */
    @DataApi class Resource(
        @PluralsRes private val resId: Int,
        private val type: Type = Type.STRING
    ) : DeferredPlurals {
        /**
         * Resolve [resId] to text with the given [context].
         *
         * If [type] is [Type.STRING], resolves a localized string. If [type] is [Type.TEXT], resolves a localized,
         * styled [CharSequence].
         *
         * @see android.content.res.Resources.getQuantityString
         * @see android.content.res.Resources.getQuantityText
         */
        override fun resolve(context: Context, quantity: Int) = when (type) {
            Type.STRING -> context.resources.getQuantityString(resId, quantity)
            Type.TEXT -> context.resources.getQuantityText(resId, quantity)
        }

        /**
         * The type of text resource to resolve.
         */
        enum class Type {
            STRING, TEXT
        }
    }
}
