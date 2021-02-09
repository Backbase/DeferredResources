package com.backbase.deferredresources.test

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.common.truth.Truth.assertThat

/**
 * Creates a [Bundle] and puts [parcelable] in that bundle. Writes the bundle to a [Parcel], and then creates a bundle
 * from the same parcel. Gets the parceled instance of [T] from the new bundle and passes that to [validateResult].
 *
 * By default, [validateResult] tests that the result is equal to [parcelable] and that their hash codes are equal.
 */
internal inline fun <T : Parcelable> testParcelableThroughBundle(
    parcelable: T,
    validateResult: (T) -> Unit = { result ->
        assertThat(result).isEqualTo(parcelable)
        assertThat(result.hashCode()).isEqualTo(parcelable.hashCode())
    }
) {
    val key = "testParcelableThroughBundle"
    val beforeBundle = Bundle()
    beforeBundle.putParcelable(key, parcelable)

    val parcel = Parcel.obtain()
    try {
        beforeBundle.writeToParcel(parcel, beforeBundle.describeContents())

        parcel.setDataPosition(0)

        val afterBundle = Bundle.CREATOR.createFromParcel(parcel).apply {
            classLoader = parcelable.javaClass.classLoader
        }
        val after = afterBundle.getParcelable<T>(key)
        assertThat(after).isNotNull()

        validateResult(after!!)
    } finally {
        parcel.recycle()
    }
}
