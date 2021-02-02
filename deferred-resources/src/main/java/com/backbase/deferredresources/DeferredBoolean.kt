package com.backbase.deferredresources

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import com.backbase.deferredresources.boolean.ParcelableDeferredBoolean
import com.backbase.deferredresources.internal.resolveAttribute
import dev.drewhamilton.extracare.DataApi
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for resolving a boolean on demand.
 */
public interface DeferredBoolean {

    /**
     * Resolve the boolean.
     */
    public fun resolve(context: Context): Boolean

    /**
     * A wrapper for a constant boolean [value].
     */
    @Parcelize
    @DataApi public class Constant(
        private val value: Boolean
    ) : ParcelableDeferredBoolean {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Boolean = value
    }

    /**
     * A wrapper for a [BoolRes] [resId].
     */
    @Parcelize
    @DataApi public class Resource(
        @BoolRes private val resId: Int
    ) : ParcelableDeferredBoolean {
        /**
         * Resolve [resId] to a boolean with the given [context].
         */
        override fun resolve(context: Context): Boolean = context.resources.getBoolean(resId)
    }

    @Parcelize
    @DataApi public class Attribute(
        @AttrRes private val resId: Int
    ) : ParcelableDeferredBoolean {

        // Re-used every time the boolean is resolved, for efficiency
        @IgnoredOnParcel
        private val reusedTypedValue = TypedValue()

        /**
         * Resolve [resId] to a boolean with the given [context]'s theme.
         *
         * @throws IllegalArgumentException if [resId] cannot be resolved to a boolean.
         */
        override fun resolve(context: Context): Boolean = context.resolveBooleanAttribute(resId)

        private fun Context.resolveBooleanAttribute(@AttrRes resId: Int): Boolean =
            resolveAttribute(resId, "boolean", reusedTypedValue, TypedValue.TYPE_INT_BOOLEAN) {
                data != 0
            }
    }
}
