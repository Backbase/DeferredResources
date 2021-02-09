package com.backbase.deferredresources.color

import android.os.Parcelable
import androidx.annotation.ColorInt
import com.backbase.deferredresources.DeferredColor

/**
 * A [Parcelable] wrapper for resolving a [ColorInt] color on demand.
 */
public interface ParcelableDeferredColor : DeferredColor, Parcelable
