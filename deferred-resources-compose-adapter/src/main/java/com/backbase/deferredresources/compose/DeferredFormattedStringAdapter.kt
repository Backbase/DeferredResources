package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedString

/**
 * Resolve [deferredFormattedString] with the given [formatArgs], remembering the resulting value as long as the current
 * [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(
    deferredFormattedString: DeferredFormattedString,
    vararg formatArgs: Any
): String {
    val context = LocalContext.current
    return remember(context, deferredFormattedString, *formatArgs) {
        deferredFormattedString.resolve(context, *formatArgs)
    }
}
