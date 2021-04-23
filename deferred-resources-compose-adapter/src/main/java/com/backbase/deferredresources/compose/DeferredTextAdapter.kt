package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.text.resolveToString

/**
 * Resolve [deferredText], remembering the resulting value as long as the current [LocalContext] doesn't change.
 *
 * Note: Currently, style elements from resource-resolved text are not kept.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedAnnotatedString(deferredText: DeferredText): AnnotatedString {
    val context = LocalContext.current
    return remember(context, deferredText) {
        when (val text = deferredText.resolve(context)) {
            is AnnotatedString -> text
            // TODO: SpannedString
            else -> AnnotatedString(text.toString())
        }
    }
}

/**
 * Resolve [deferredText] to a plain string, remembering the resulting value as long as the current [LocalContext]
 * doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(deferredText: DeferredText): String {
    val context = LocalContext.current
    return remember(context, deferredText) {
        deferredText.resolveToString(context)
    }
}
