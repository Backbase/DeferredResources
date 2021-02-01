package com.backbase.deferredresources.text

import android.os.Parcelable
import com.backbase.deferredresources.DeferredPlurals

/**
 * A [Parcelable] wrapper for resolving pluralized text on demand.
 */
public interface ParcelableDeferredPlurals : DeferredPlurals, Parcelable
