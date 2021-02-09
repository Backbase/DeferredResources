package com.backbase.deferredresources.text

import android.os.Parcelable
import com.backbase.deferredresources.DeferredTextArray

/**
 * A [Parcelable] wrapper for resolving a text array on demand.
 */
public interface ParcelableDeferredTextArray : DeferredTextArray, Parcelable
