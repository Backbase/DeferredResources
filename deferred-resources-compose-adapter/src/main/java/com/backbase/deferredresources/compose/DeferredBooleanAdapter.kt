package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredBoolean

/**
 * Resolve the [DeferredBoolean] using the current composition-local Context.
 */
@ExperimentalComposeAdapter
@Composable public fun DeferredBoolean.resolve(): Boolean = resolve(LocalContext.current)
