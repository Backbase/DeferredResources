package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedPlurals

/**
 * Resolve the [DeferredFormattedPlurals] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedPlurals.resolve(
    quantity: Int,
    vararg formatArgs: Any = arrayOf(quantity)
): String = resolve(LocalContext.current, quantity, *formatArgs)
