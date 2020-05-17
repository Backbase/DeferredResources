package com.backbase.deferredresources.internal

import android.os.Handler
import android.os.Looper

/**
 * Return this [Handler] or a new main thread [Handler] if this one is null.
 */
internal fun Handler?.orUiHandler(): Handler = this ?: Handler(Looper.getMainLooper())
