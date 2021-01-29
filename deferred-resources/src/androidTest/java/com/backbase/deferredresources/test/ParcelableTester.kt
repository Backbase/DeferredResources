package com.backbase.deferredresources.test

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.common.truth.Truth.assertThat
import org.junit.rules.ExternalResource

/**
 * A test rule which allows verification of a Parcelable implementation
 */
internal class ParcelableTester : ExternalResource() {

    private val parcel: Parcel get() = checkNotNull(_parcel) { "Can only access parcel during a test." }

    fun <T : Parcelable> testParcelableThroughBundle(parcelable: T) {
        val key = "testParcelsThroughBundle"

        val beforeBundle = Bundle()
        beforeBundle.putParcelable(key, parcelable)
        beforeBundle.writeToParcel(parcel, beforeBundle.describeContents())

        parcel.setDataPosition(0)

        val afterBundle = Bundle.CREATOR.createFromParcel(parcel).apply {
            classLoader = parcelable.javaClass.classLoader
        }
        val after = afterBundle.getParcelable<T>(key)
        assertThat(after).isEqualTo(parcelable)
        checkNotNull(after)
        assertThat(after.hashCode()).isEqualTo(parcelable.hashCode())
    }

    private var _parcel: Parcel? = null

    override fun before() {
        _parcel = Parcel.obtain()
    }

    override fun after() {
        _parcel!!.recycle()
        _parcel = null
    }
}
