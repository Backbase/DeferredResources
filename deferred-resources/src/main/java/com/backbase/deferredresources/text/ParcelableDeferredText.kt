package com.backbase.deferredresources.text

import android.os.Parcelable
import com.backbase.deferredresources.DeferredText

/**
 * A [Parcelable] wrapper for resolving text on demand.
 */
public interface ParcelableDeferredText : DeferredText, Parcelable
