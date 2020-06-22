@file:JvmName("DeferredTextUtils")

package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredText

/**
 * Resolve a [DeferredText] to a string rather than a CharSequence by calling [toString] on the resolved value. Any
 * styling information from the original CharSequence will typically be removed from the resolved string.
 */
fun DeferredText.resolveToString(context: Context) = resolve(context).toString()
