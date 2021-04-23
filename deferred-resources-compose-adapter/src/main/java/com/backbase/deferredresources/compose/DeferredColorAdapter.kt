package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredColor

/**
 * Instantiate a [DeferredColor.Constant] with a Compose [Color] [value].
 */
@ExperimentalComposeAdapter
@Suppress("FunctionName") // Factory
public fun DeferredColor.Companion.Constant(value: Color): DeferredColor.Constant =
    DeferredColor.Constant(value = value.toArgb())

/**
 * Resolve [deferredColor], remembering the resulting value as long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedColor(deferredColor: DeferredColor): Color {
    val context = LocalContext.current
    return remember(context, deferredColor) {
        Color(deferredColor.resolve(context))
    }
}
