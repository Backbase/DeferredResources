@file:JvmName("DeferredResourcesImageViewUtils")

package com.backbase.deferredresources.viewx

import android.graphics.PorterDuff
import android.widget.ImageView
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable

/**
 * Resolves [deferredDrawable] and sets the resolved drawable as the content of this ImageView.
 */
public fun ImageView.setImageDrawable(deferredDrawable: DeferredDrawable): Unit =
    setImageDrawable(deferredDrawable.resolve(context))

/**
 * Resolve [deferredColor] and set the resolved color as a tinting option for the image. Assumes
 * [PorterDuff.Mode.SRC_ATOP] blending mode.
 */
public fun ImageView.setColorFilter(deferredColor: DeferredColor): Unit =
    setColorFilter(deferredColor.resolve(context))

/**
 * Resolve [deferredColor] and set the resolved color as a tinting option for the image and the given blending [mode].
 */
public fun ImageView.setColorFilter(deferredColor: DeferredColor, mode: PorterDuff.Mode): Unit =
    setColorFilter(deferredColor.resolve(context), mode)
