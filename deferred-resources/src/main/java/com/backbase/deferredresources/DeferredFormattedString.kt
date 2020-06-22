package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.StringRes
import com.backbase.deferredresources.internal.primaryLocale
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a formatted string on demand.
 */
interface DeferredFormattedString {

    /**
     * Resolve the string with the supplied [formatArgs].
     */
    fun resolve(context: Context, vararg formatArgs: Any): String

    /**
     * A wrapper for a constant format-able [format] string.
     */
    @DataApi class Constant(
        private val format: String
    ) : DeferredFormattedString {
        /**
         * Always resolves [format] with the supplied [formatArgs] and the primary locale from [context].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  =
            String.format(context.primaryLocale, format, *formatArgs)
    }

    /**
     * A wrapper for a format-able [StringRes] [resId].
     */
    @DataApi class Resource(
        @StringRes private val resId: Int
    ) : DeferredFormattedString {

        /**
         * Resolve [resId] to a formatted string with the given [context] and [formatArgs].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  = context.getString(resId, *formatArgs)
    }
}
