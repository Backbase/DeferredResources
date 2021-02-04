package com.backbase.deferredresources.dimension

import android.os.Parcelable
import com.backbase.deferredresources.DeferredDimension

/**
 * A [Parcelable] wrapper for resolving an integer dimension on demand.
 */
public interface ParcelableDeferredDimension : DeferredDimension, Parcelable
