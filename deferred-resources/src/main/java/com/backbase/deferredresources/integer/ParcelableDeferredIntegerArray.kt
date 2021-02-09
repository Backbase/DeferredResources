package com.backbase.deferredresources.integer

import android.os.Parcelable
import com.backbase.deferredresources.DeferredIntegerArray

/**
 * A [Parcelable] wrapper for resolving an integer array on demand.
 */
public interface ParcelableDeferredIntegerArray : DeferredIntegerArray, Parcelable
