package com.backbase.deferredresources.compose

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.color.ParcelableDeferredColor
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * Resolve the [DeferredColor] to a [Color] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredColor.resolve(): Color = when (this) {
    is ComposeDeferredColor -> color
    else -> Color(resolve(LocalContext.current))
}

/**
 * A [DeferredColor] based on a single Compose [color].
 */
@ExperimentalDeferredResourcesComposeSupport
@Parcelize
@Poko public class ComposeDeferredColor(
    internal val color: Color
) : ParcelableDeferredColor {
    /**
     * Resolve the Compose [Color] to a [ColorInt].
     */
    @ColorInt override fun resolve(context: Context): Int = color.toArgb()

    /**
     * Resolve the Compose [Color] to a single-state [ColorStateList].
     */
    override fun resolveToStateList(context: Context): ColorStateList = ColorStateList.valueOf(resolve(context))
}
