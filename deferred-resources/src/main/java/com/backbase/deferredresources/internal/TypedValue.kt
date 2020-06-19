package com.backbase.deferredresources.internal

import android.util.TypedValue

/**
 * An empty [TypedValue] used to reset "Attribute" implementations' internal [TypedValue]s after resolution.
 *
 * Mutating this value is incorrect behavior and may cause unexpected bugs.
 */
internal val EMPTY_TYPED_VALUE: TypedValue = TypedValue()
