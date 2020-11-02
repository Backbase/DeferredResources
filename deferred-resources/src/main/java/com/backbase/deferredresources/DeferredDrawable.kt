package com.backbase.deferredresources

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving a [Drawable] on demand.
 */
public interface DeferredDrawable {

    /**
     * Resolve the [Drawable].
     */
    public fun resolve(context: Context): Drawable?

    /**
     * A wrapper for a constant Drawable [value].
     */
    @DataApi public class Constant(
        private val value: Drawable?
    ) : DeferredDrawable {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): Drawable? = value
    }

    /**
     * A wrapper for a [Drawable] [resId]. Optionally [mutate]s each resolved Drawable. Optionally provide
     * [transformations] (such as [Drawable.setTint]) to apply each time the Drawable is resolved.
     *
     * If [transformations] are supplied, [mutate] should be true.
     */
    @DataApi public class Resource @JvmOverloads constructor(
        @DrawableRes private val resId: Int,
        private val mutate: Boolean = true,
        private val transformations: Drawable.(Context) -> Unit = {}
    ) : DeferredDrawable {

        /**
         * Convenience constructor that sets [mutate] to true when [transformations] are supplied.
         */
        public constructor(
            @DrawableRes resId: Int,
            transformations: Drawable.(Context) -> Unit
        ) : this(resId, mutate = true, transformations = transformations)

        /**
         * Resolve [resId] to a [Drawable] with the given [context]. If [mutate] is true, returns the result of
         * [Drawable.mutate] instead of the original Drawable. Applies [transformations] before returning.
         */
        override fun resolve(context: Context): Drawable? {
            val original = AppCompatResources.getDrawable(context, resId)
            val drawable = if (mutate) original?.mutate() else original
            return drawable?.apply { transformations(context) }
        }
    }

    /**
     * A wrapper for a [Drawable] [resId]. Optionally [mutate]s each resolved Drawable. Optionally provide
     * [transformations] (such as [Drawable.setTint]) to apply each time the Drawable is resolved.
     *
     * If [transformations] are supplied, [mutate] should be true.
     */
    @DataApi public class Attribute @JvmOverloads constructor(
        @AttrRes private val resId: Int,
        private val mutate: Boolean = true,
        private val transformations: Drawable.(Context) -> Unit = {}
    ) : DeferredDrawable {

        /**
         * Convenience constructor that sets [mutate] to true when [transformations] are supplied.
         */
        public constructor(
            @AttrRes resId: Int,
            transformations: Drawable.(Context) -> Unit
        ) : this(resId, mutate = true, transformations = transformations)

        /**
         * Resolve [resId] to a [Drawable] with the given [context]. If [mutate] is true, returns the result of
         * [Drawable.mutate] instead of the original Drawable. Applies [transformations] before returning.
         */
        override fun resolve(context: Context): Drawable? {
            val original = AppCompatResources.getDrawable(context, TypedValue().apply {
                context.theme.resolveAttribute(resId, this, true)
            }.resourceId)
            val drawable = if (mutate) original?.mutate() else original
            return drawable?.apply { transformations(context) }
        }
    }
}
