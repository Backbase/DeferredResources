package com.backbase.deferredresources.internal

import kotlin.math.roundToInt

/**
 * Convert a float to an integer value for use as a size. The exact value is rounded, and non-zero exact values always
 * result in a size of at least one.
 */
internal fun Float.toSize(): Int {
    val rounded = roundToInt()
    return when {
        rounded != 0 -> rounded
        this == 0f -> 0
        this > 0f -> 1
        else -> -1
    }
}
