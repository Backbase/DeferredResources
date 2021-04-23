package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.backbase.deferredresources.DeferredPlurals

/**
 * Resolve [deferredPlurals], remembering the resulting value as long as the current [LocalContext] doesn't change.
 *
 * Note: Currently, style elements from resource-resolved text are not kept.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedAnnotatedString(
    deferredPlurals: DeferredPlurals,
    quantity: Int,
): AnnotatedString {
    val context = LocalContext.current
    return remember(context, quantity, deferredPlurals) {
        when (val text = deferredPlurals.resolve(context, quantity)) {
            is AnnotatedString -> text
            // TODO: SpannedString
            else -> AnnotatedString(text.toString())
        }
    }
}
