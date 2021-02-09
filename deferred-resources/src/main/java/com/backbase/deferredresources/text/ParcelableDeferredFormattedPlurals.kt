package com.backbase.deferredresources.text

import android.os.Parcelable
import com.backbase.deferredresources.DeferredFormattedPlurals

/**
 * A [Parcelable] wrapper for resolving format-able pluralized text on demand.
 */
public interface ParcelableDeferredFormattedPlurals : DeferredFormattedPlurals, Parcelable
