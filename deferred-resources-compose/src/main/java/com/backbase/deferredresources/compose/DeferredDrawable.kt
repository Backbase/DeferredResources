package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredDrawable
import com.google.accompanist.imageloading.rememberDrawablePainter

/**
 * Resolve a [deferredDrawable] into a Compose [Painter], remembering the resulting painter as long as the current
 * [LocalContext] and [deferredDrawable] don't change.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun rememberResolvedPainter(deferredDrawable: DeferredDrawable): Painter {
    val context = LocalContext.current
    val drawable = remember(context, deferredDrawable) {
        deferredDrawable.resolve(context)
    }

    return when (drawable) {
        null -> remember { ColorPainter(Color.Transparent) }
        else -> rememberDrawablePainter(drawable)
    }
}
