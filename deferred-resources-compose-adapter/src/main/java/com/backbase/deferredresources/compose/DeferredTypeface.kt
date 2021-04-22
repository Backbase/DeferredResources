package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import com.backbase.deferredresources.DeferredTypeface

/**
 * Resolve a [deferredTypeface] to a Compose [FontFamily], remembering the resulting font family instance as long as the
 * current [LocalContext] and [deferredTypeface] don't change.
 *
 * If the given [deferredTypeface] resolves to a null typeface, returns null.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedFontFamily(deferredTypeface: DeferredTypeface): FontFamily? {
    val context = LocalContext.current
    val typeface = remember(context, deferredTypeface) {
        deferredTypeface.resolve(context)
    }

    return remember(typeface) {
        if (typeface == null) null else FontFamily(typeface)
    }
}
