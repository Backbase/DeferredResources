package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredColor

/**
 * Instantiate a [DeferredColor.Constant] with a Compose [Color] [value].
 */
@ExperimentalDeferredResourcesComposeSupport
@Suppress("FunctionName") // Factory
public fun DeferredColor.Companion.Constant(value: Color): DeferredColor.Constant =
    DeferredColor.Constant(value = value.toArgb())

/**
 * Resolve the [DeferredColor] to a [Color] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredColor.resolve(): Color = Color(resolve(LocalContext.current))
