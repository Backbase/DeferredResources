package com.backbase.deferredresources.color

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.context
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class SdkIntDeferredColorTest {

    @ColorInt private val sdk14 = Color.parseColor("#000014")
    @ColorInt private val sdk15 = Color.parseColor("#000015")
    @ColorInt private val sdk16 = Color.parseColor("#000016")
    @ColorInt private val sdk17 = Color.parseColor("#000017")
    @ColorInt private val sdk18 = Color.parseColor("#000018")
    @ColorInt private val sdk19 = Color.parseColor("#000019")
    @ColorInt private val sdk20 = Color.parseColor("#000020")
    @ColorInt private val sdk21 = Color.parseColor("#000021")
    @ColorInt private val sdk22 = Color.parseColor("#000022")
    @ColorInt private val sdk23 = Color.parseColor("#000023")
    @ColorInt private val sdk24 = Color.parseColor("#000024")
    @ColorInt private val sdk25 = Color.parseColor("#000025")
    @ColorInt private val sdk26 = Color.parseColor("#000026")
    @ColorInt private val sdk27 = Color.parseColor("#000027")
    @ColorInt private val sdk28 = Color.parseColor("#000028")
    @ColorInt private val sdk29 = Color.parseColor("#000029")
    @ColorInt private val sdk30 = Color.parseColor("#000030")

    @Test fun allValuesDefined_resolvesToCorrectSource() {
        val deferred = SdkIntDeferredColor(
            minSdk = sdk14.asDeferredColor(),
            sdk15 = sdk15.asDeferredColor(),
            sdk16 = sdk16.asDeferredColor(),
            sdk17 = sdk17.asDeferredColor(),
            sdk18 = sdk18.asDeferredColor(),
            sdk19 = sdk19.asDeferredColor(),
            sdk20 = sdk20.asDeferredColor(),
            sdk21 = sdk21.asDeferredColor(),
            sdk22 = sdk22.asDeferredColor(),
            sdk23 = sdk23.asDeferredColor(),
            sdk24 = sdk24.asDeferredColor(),
            sdk25 = sdk25.asDeferredColor(),
            sdk26 = sdk26.asDeferredColor(),
            sdk27 = sdk27.asDeferredColor(),
            sdk28 = sdk28.asDeferredColor(),
            sdk29 = sdk29.asDeferredColor(),
            sdk30 = sdk30.asDeferredColor(),
        )
        val resolvedColor = deferred.resolve(context)
        val resolvedStateList = deferred.resolveToStateList(context)

        val expectedColor = when (val sdkInt = Build.VERSION.SDK_INT) {
            14 -> sdk14
            15 -> sdk15
            16 -> sdk16
            17 -> sdk17
            18 -> sdk18
            19 -> sdk19
            20 -> sdk20
            21 -> sdk21
            22 -> sdk22
            23 -> sdk23
            24 -> sdk24
            25 -> sdk25
            26 -> sdk26
            27 -> sdk27
            28 -> sdk28
            29 -> sdk29
            30 -> sdk30
            else -> if (sdkInt > 30) sdk30 else sdk14
        }

        assertThat(resolvedColor).isEqualTo(expectedColor)
        assertThat(resolvedStateList.defaultColor).isEqualTo(ColorStateList.valueOf(expectedColor).defaultColor)
    }

    @Test fun someValuesDefined_resolvesToNearestLowerSdkSource() {
        val deferred = SdkIntDeferredColor(
            minSdk = sdk15.asDeferredColor(),
            sdk21 = sdk21.asDeferredColor(),
            sdk23 = sdk23.asDeferredColor(),
        )

        val resolvedColor = deferred.resolve(context)
        val resolvedStateList = deferred.resolveToStateList(context)

        val expectedColor = when (val sdkInt = Build.VERSION.SDK_INT) {
            14, 15, 16, 17, 18, 19, 20 -> sdk15
            21, 22 -> sdk21
            else -> if (sdkInt >= 23) sdk23 else sdk15
        }

        assertThat(resolvedColor).isEqualTo(expectedColor)
        assertThat(resolvedStateList.defaultColor).isEqualTo(ColorStateList.valueOf(expectedColor).defaultColor)
    }

    private fun Int.asDeferredColor() = DeferredColor.Constant(this)
}
