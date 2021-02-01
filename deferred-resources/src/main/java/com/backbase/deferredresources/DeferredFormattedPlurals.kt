package com.backbase.deferredresources

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.PluralRules
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import com.backbase.deferredresources.internal.PluralRulesCompat
import com.backbase.deferredresources.internal.primaryLocale
import com.backbase.deferredresources.text.ParcelableDeferredFormattedPlurals
import dev.drewhamilton.extracare.DataApi
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
     * A wrapper for constant format-able pluralized text. [zero], [one], [two], [few], and [many] are locale-specific,
     * quantity-specific text values, while [other] is the fallback.
     */
    // Primary constructor is internal rather than private so the generated Creator can access it
    @Parcelize
    @DataApi public class Constant internal constructor(
        private val other: String,
        private val zero: String = other,
        private val one: String = other,
        private val two: String = other,
        private val few: String = other,
        private val many: String = other,
        // typeName is a String to allow @Parcelize to work as expected on API < 21. Directly using
        //  PluralRules.PluralType, even if null, causes a VerifyError in the constructor at runtime on API < 21.
        private val typeName: String?
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
        ) : this(other, zero, one, two, few, many, typeName = type?.name)

        /**
         * Constructor for API < 24. "CARDINAL" plural type will be used implicitly.
         */
        @SuppressLint("NewApi") // Safely calls API 24 constructor with null
        public constructor(
            other: String,
            zero: String = other,
            one: String = other,
            two: String = other,
            few: String = other,
            many: String = other
        ) : this(other, zero, one, two, few, many, typeName = null)

        /**
         * Resolves and formats one of [zero], [one], [two], [few], [many], or [other] depending on the primary locale
         * from [context], the given [quantity], and [formatArgs].
         */
        override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String {
            val pluralRules = if (typeName == null)
                PluralRulesCompat.forContext(context)
            else
                @Suppress("NewApi") // If type is non-null, we're on API 24+
                PluralRulesCompat.forContext(context, PluralRules.PluralType.valueOf(typeName))

            val unformatted = when (pluralRules.select(quantity)) {
                PluralRulesCompat.KEYWORD_ZERO -> zero
                PluralRulesCompat.KEYWORD_ONE -> one
                PluralRulesCompat.KEYWORD_TWO -> two
                PluralRulesCompat.KEYWORD_FEW -> few
                PluralRulesCompat.KEYWORD_MANY -> many
                else -> other
            }
            return String.format(context.primaryLocale, unformatted, *formatArgs)
        }
    }

    /**
     * A wrapper for a format-able [PluralsRes] [resId].
     */
    @Parcelize
    @DataApi public class Resource(
        @PluralsRes private val resId: Int
    ) : ParcelableDeferredFormattedPlurals {
        /**
         * Resolve [resId] to a formatted string with the given [context], [quantity], and [formatArgs].
         */
        override fun resolve(context: Context, quantity: Int, vararg formatArgs: Any): String =
            context.resources.getQuantityString(resId, quantity, *formatArgs)
    }
}
