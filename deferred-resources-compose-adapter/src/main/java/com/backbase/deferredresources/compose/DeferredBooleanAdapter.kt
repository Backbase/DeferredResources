package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredBoolean

/**
 * Resolve [deferredBoolean], remembering the resulting value as long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedValue(deferredBoolean: DeferredBoolean): Boolean {
    val context = LocalContext.current
    return remember(context, deferredBoolean) {
        deferredBoolean.resolve(context)
    }
}
