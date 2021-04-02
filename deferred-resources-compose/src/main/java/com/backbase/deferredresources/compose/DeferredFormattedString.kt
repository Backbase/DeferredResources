package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedString

/**
 * Resolve the [DeferredFormattedString] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedString.resolve(vararg formatArgs: Any): String =
    resolve(LocalContext.current, *formatArgs)
