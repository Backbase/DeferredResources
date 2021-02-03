package com.backbase.deferredresources.bool

import android.os.Parcelable
import com.backbase.deferredresources.DeferredBoolean

/**
 * A [Parcelable] wrapper for resolving a boolean on demand.
 */
public interface ParcelableDeferredBoolean : DeferredBoolean, Parcelable
