package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredPlurals

/**
 * Resolve the [DeferredPlurals] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredPlurals.resolve(quantity: Int): CharSequence =
    resolve(LocalContext.current, quantity)
