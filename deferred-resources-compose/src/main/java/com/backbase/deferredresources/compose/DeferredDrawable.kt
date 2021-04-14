package com.backbase.deferredresources.compose

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredDrawable
import com.google.accompanist.imageloading.AndroidDrawablePainter
import com.google.accompanist.imageloading.toPainter

/**
 * Wrap a [DeferredDrawable] into a Compose [Painter].
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun rememberDeferredDrawablePainter(deferredDrawable: DeferredDrawable): Painter {
    val context = LocalContext.current
    val drawable = remember(deferredDrawable) {
        deferredDrawable.resolve(context)
    }

    // region TODO: Replace with Accompanist `rememberDrawablePainter` when available
    val painter = remember(drawable) {
        drawable?.toPainter() ?: ColorPainter(Color.Transparent)
    }

    val invalidateTick = remember {
        mutableStateOf(0)
    }
    val callback = object : Drawable.Callback {
        override fun invalidateDrawable(d: Drawable) {
            // Update the tick so that we get re-drawn
            invalidateTick.value++
        }

        override fun scheduleDrawable(d: Drawable, what: Runnable, time: Long) {
            MAIN_HANDLER.postAtTime(what, time)
        }

        override fun unscheduleDrawable(d: Drawable, what: Runnable) {
            MAIN_HANDLER.removeCallbacks(what)
        }
    }

    DisposableEffect(painter) {
        if (painter is AndroidDrawablePainter) {
            checkNotNull(drawable)
            drawable.callback = callback
            drawable.setVisible(true, true)
            if (drawable is Animatable) drawable.start()
        }
        onDispose {
            if (painter is AndroidDrawablePainter) {
                checkNotNull(drawable)
                if (drawable is Animatable) drawable.stop()
                drawable.setVisible(false, false)
                drawable.callback = null
            }
        }
    }

    return painter
    //endregion
}

private val MAIN_HANDLER by lazy(LazyThreadSafetyMode.NONE) {
    Handler(Looper.getMainLooper())
}
