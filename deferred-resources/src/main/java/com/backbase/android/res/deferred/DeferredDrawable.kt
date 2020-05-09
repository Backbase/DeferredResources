package com.backbase.android.res.deferred

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a [Drawable] on demand.
 */
interface DeferredDrawable {

    /**
     * Resolve the [Drawable].
     */
    fun resolve(context: Context): Drawable?

    /**
     * A wrapper for a constant Drawable [value].
     */
    @DataApi class Constant(
        private val value: Drawable?
    ) : DeferredDrawable {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context) = value
    }

    /**
     * A wrapper for a [Drawable] [resId]. Optionally [mutate]s each resolved Drawable. Optionally provide
     * [transformations] (such as [Drawable.setTint]) to apply each time the Drawable is resolved.
     *
     * If [transformations] are supplied, [mutate] should be true.
     */
    @DataApi class Resource @JvmOverloads constructor(
        @DrawableRes private val resId: Int,
        private val mutate: Boolean = true,
        private val transformations: Drawable.() -> Unit = {}
    ) : DeferredDrawable {

        /**
         * Convenience constructor that sets [mutate] to true when [transformations] are supplied.
         */
        constructor(
            @DrawableRes resId: Int,
            transformations: Drawable.() -> Unit
        ) : this(resId, mutate = true, transformations = transformations)

        /**
         * Resolve [resId] to a [Drawable] with the given [context]. If [mutate] is true, returns the result of
         * [Drawable.mutate] instead of the original Drawable. Applies [transformations] before returning.
         */
        override fun resolve(context: Context): Drawable? {
            val original = ContextCompat.getDrawable(context, resId)
            val drawable = if (mutate) original?.mutate() else original
            return drawable?.apply(transformations)
        }
    }
}
