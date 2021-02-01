package com.backbase.deferredresources

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.PluralRules
import android.os.Build
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import com.backbase.deferredresources.internal.PluralRulesCompat
import com.backbase.deferredresources.text.ParcelableDeferredPlurals
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving pluralized text on demand.
 */
public interface DeferredPlurals {

    /**
     * Resolve the text for the given [quantity].
     */
    public fun resolve(context: Context, quantity: Int): CharSequence

    /**
     * A wrapper for constant pluralized text. [zero], [one], [two], [few], and [many] are locale-specific,
     * quantity-specific text values, while [other] is the fallback.
     */
    // Primary constructor is internal rather than private so the generated Creator can access it
    @Parcelize
    @DataApi public class Constant internal constructor(
        private val other: CharSequence,
        private val zero: CharSequence = other,
        private val one: CharSequence = other,
        private val two: CharSequence = other,
        private val few: CharSequence = other,
        private val many: CharSequence = other,
        private val typeName: String?
    ) : ParcelableDeferredPlurals {

        /**
         * Constructor for API 24+. [type] can be supplied to choose between [PluralRules.PluralType.CARDINAL] and
         * [PluralRules.PluralType.ORDINAL]. If null, the system default of [PluralRules.PluralType.CARDINAL] is used
         * implicitly.
         */
        @RequiresApi(24)
        public constructor(
            other: CharSequence,
            zero: CharSequence = other,
            one: CharSequence = other,
            two: CharSequence = other,
            few: CharSequence = other,
            many: CharSequence = other,
            type: PluralRules.PluralType?
        ) : this(other, zero, one, two, few, many, typeName = type?.name)

        /**
         * Constructor for APIs < 24. "CARDINAL" plural type will be used implicitly.
         */
        @SuppressLint("NewApi") // Safely calls API 24 constructor with null
        public constructor(
            other: CharSequence,
            zero: CharSequence = other,
            one: CharSequence = other,
            two: CharSequence = other,
            few: CharSequence = other,
            many: CharSequence = other
        ) : this(other, zero, one, two, few, many, typeName = null)

        /**
         * Resolves to one of [zero], [one], [two], [few], [many], or [other] depending on the device's primary locale
         * and the given [quantity].
         */
        override fun resolve(context: Context, quantity: Int): CharSequence {
            val pluralRules = if (typeName == null || Build.VERSION.SDK_INT < 24)
                PluralRulesCompat.forContext(context)
            else
                PluralRulesCompat.forContext(context, PluralRules.PluralType.valueOf(typeName))

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
    @Parcelize
    @DataApi public class Resource(
        @PluralsRes private val resId: Int,
        private val type: Type = Type.STRING
    ) : ParcelableDeferredPlurals {
        /**
         * Resolve [resId] to text with the given [context].
         *
         * If [type] is [Type.STRING], resolves a localized string. If [type] is [Type.TEXT], resolves a localized,
         * styled [CharSequence].
         *
         * @see android.content.res.Resources.getQuantityString
         * @see android.content.res.Resources.getQuantityText
         */
        override fun resolve(context: Context, quantity: Int): CharSequence = when (type) {
            Type.STRING -> context.resources.getQuantityString(resId, quantity)
            Type.TEXT -> context.resources.getQuantityText(resId, quantity)
        }

        /**
         * The type of text resource to resolve.
         */
        public enum class Type {
            STRING, TEXT
        }
    }
}
