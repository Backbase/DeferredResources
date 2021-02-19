package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.StringRes
import com.backbase.deferredresources.internal.primaryLocale
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving a formatted string on demand.
 */
public interface DeferredFormattedString {

    /**
     * Resolve the string with the supplied [formatArgs].
     */
    public fun resolve(context: Context, vararg formatArgs: Any): String

    /**
     * A wrapper for a constant format-able [format] string.
     */
    @Parcelize
    @Poko public class Constant(
        private val format: String
    ) : ParcelableDeferredFormattedString {
        /**
         * Always resolves [format] with the supplied [formatArgs] and the primary locale from [context].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  =
            String.format(context.primaryLocale, format, *formatArgs)
    }

    /**
     * A wrapper for a format-able [StringRes] [resId].
     */
    @Parcelize
    @Poko public class Resource(
        @StringRes private val resId: Int
    ) : ParcelableDeferredFormattedString {

        /**
         * Resolve [resId] to a formatted string with the given [context] and [formatArgs].
         */
        override fun resolve(context: Context, vararg formatArgs: Any): String  = context.getString(resId, *formatArgs)
    }
}
