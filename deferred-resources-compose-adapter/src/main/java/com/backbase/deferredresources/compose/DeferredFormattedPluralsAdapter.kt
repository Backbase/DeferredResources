package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedPlurals

/**
 * Resolve [deferredFormattedPlurals] with the given [quantity] and using [quantity] as the only format arg, remembering
 * the resulting value as long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(
    deferredFormattedPlurals: DeferredFormattedPlurals,
    quantity: Int,
): String = rememberResolvedString(deferredFormattedPlurals, quantity, quantity)

/**
 * Resolve [deferredFormattedPlurals] with the given [quantity] and [formatArgs], remembering the resulting value as
 * long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(
    deferredFormattedPlurals: DeferredFormattedPlurals,
    quantity: Int,
    vararg formatArgs: Any,
): String {
    val context = LocalContext.current
    return remember(context, quantity, deferredFormattedPlurals, *formatArgs) {
        deferredFormattedPlurals.resolve(context, quantity, *formatArgs)
    }
}
