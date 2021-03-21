package com.backbase.deferredresources.test

import android.os.Parcel
import android.os.Parcelable
import com.google.common.truth.Truth.assertThat

/**
 * Writes [parcelable] to a [Parcel], marshalls it to bytes, unmarshalls those bytes from another [Parcel], and passes
 * the resulting instance to [validateResult].
 *
 * By default, [validateResult] tests that the result is equal to [parcelable] and that their hash codes are equal.
 */
// Adapted from https://gist.github.com/shekibobo/8ad3b19e5e4ced6ad2279e23edd18a0b
internal inline fun <reified T : Parcelable> testParcelable(
    parcelable: T,
    validateResult: (T?) -> Unit = { result ->
        assertThat(result).isEqualTo(parcelable)
        assertThat(result.hashCode()).isEqualTo(parcelable.hashCode())
    }
) {
    val after = forceParcel(parcelable)
    validateResult(after)
}

private inline fun <reified T : Parcelable> forceParcel(parcelable: T): T? {
    val bytes = Parcel.obtain().use {
        writeParcelable(parcelable, 0)
        marshall()
    }
    return Parcel.obtain().use {
        unmarshall(bytes, 0, bytes.size)
        setDataPosition(0)
        readParcelable(T::class.java.classLoader)
    }
}

private inline fun <T> Parcel.use(action: Parcel.() -> T): T = try {
    action()
} finally {
    recycle()
}
