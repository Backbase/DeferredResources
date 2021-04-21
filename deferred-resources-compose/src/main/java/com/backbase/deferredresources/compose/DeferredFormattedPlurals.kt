package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedPlurals

/**
 * Resolve the [DeferredFormattedPlurals] using the current composition-local Context, the given [quantity], and the
 * same quantity as the only format arg.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedPlurals.resolve(
    quantity: Int
): String = resolve(LocalContext.current, quantity, quantity)

/**
 * Resolve the [DeferredFormattedPlurals] using the current composition-local Context, the given [quantity], and the
 * given [formatArgs].
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedPlurals.resolve(
    quantity: Int,
    vararg formatArgs: Any,
): String = resolve(LocalContext.current, quantity, *formatArgs)
