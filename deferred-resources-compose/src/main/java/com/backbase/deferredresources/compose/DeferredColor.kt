package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredColor

/**
 * Resolve the [DeferredColor] to a [Color] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredColor.resolve(): Color = Color(resolve(LocalContext.current))
