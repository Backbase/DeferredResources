package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredInteger

/**
 * Resolve the [DeferredInteger] using the current composition-local Context.
 */
@ExperimentalComposeAdapter
@Composable public fun DeferredInteger.resolve(): Int = resolve(LocalContext.current)
