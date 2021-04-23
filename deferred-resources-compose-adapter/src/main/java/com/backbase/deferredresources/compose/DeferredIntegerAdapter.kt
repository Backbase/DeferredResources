package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredInteger

/**
 * Resolve [deferredInteger], remembering the resulting value as long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedValue(deferredInteger: DeferredInteger): Int {
    val context = LocalContext.current
    return remember(context, deferredInteger) {
        deferredInteger.resolve(context)
    }
}
