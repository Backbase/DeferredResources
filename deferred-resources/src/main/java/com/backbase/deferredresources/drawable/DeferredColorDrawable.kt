package com.backbase.deferredresources.drawable

import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import dev.drewhamilton.poko.Poko

/**
 * Convert a [DeferredColor] to a [DeferredDrawable] by wrapping the resolved color in a [ColorDrawable].
 */
@JvmSynthetic public fun DeferredColor.asDrawable(): DeferredColorDrawable = DeferredColorDrawable(this)

/**
 * Convert a [DeferredColor] to a [DeferredDrawable] by wrapping the resolved color in a [ColorDrawable].
 */
@Poko public class DeferredColorDrawable(
    private val deferredColor: DeferredColor
) : DeferredDrawable {

    /**
     * Use [context] to resolve the color and create a new [ColorDrawable] displaying that color.
     */
    override fun resolve(context: Context): ColorDrawable = ColorDrawable(deferredColor.resolve(context))
}
