package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredText

/**
 * Resolve a [DeferredText] to a string by calling [toString] on the resolved value.
 */
fun DeferredText.resolveAsString(context: Context) = resolve(context).toString()
