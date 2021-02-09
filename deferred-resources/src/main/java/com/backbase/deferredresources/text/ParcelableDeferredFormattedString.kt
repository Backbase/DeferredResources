package com.backbase.deferredresources.text

import android.os.Parcelable
import com.backbase.deferredresources.DeferredFormattedString

/**
 * A [Parcelable] wrapper for resolving a formatted string on demand.
 */
public interface ParcelableDeferredFormattedString : DeferredFormattedString, Parcelable
