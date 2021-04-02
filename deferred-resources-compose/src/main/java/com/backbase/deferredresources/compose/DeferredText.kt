package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.text.resolveToString

/**
 * Resolve the [DeferredText] using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredText.resolve(): AnnotatedString = when (val text = resolve(LocalContext.current)) {
    is AnnotatedString -> text
    // TODO: SpannedString
    else -> AnnotatedString(text.toString())
}

/**
 * Resolve the [DeferredText] to a String using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredText.resolveToString(): String = resolveToString(LocalContext.current)
