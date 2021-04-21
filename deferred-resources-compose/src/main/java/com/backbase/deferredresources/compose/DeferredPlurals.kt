package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.backbase.deferredresources.DeferredPlurals

/**
 * Resolve the [DeferredPlurals] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredPlurals.resolve(quantity: Int): AnnotatedString =
    when (val text = resolve(LocalContext.current, quantity)) {
        is AnnotatedString -> text
        // TODO: SpannedString
        else -> AnnotatedString(text.toString())
    }
