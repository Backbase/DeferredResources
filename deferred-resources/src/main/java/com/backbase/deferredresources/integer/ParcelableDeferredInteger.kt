package com.backbase.deferredresources.integer

import android.os.Parcelable
import com.backbase.deferredresources.DeferredInteger

/**
 * A [Parcelable] wrapper for resolving an integer on demand.
 */
public interface ParcelableDeferredInteger : DeferredInteger, Parcelable
