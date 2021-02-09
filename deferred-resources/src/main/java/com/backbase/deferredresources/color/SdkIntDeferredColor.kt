package com.backbase.deferredresources.color

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import androidx.annotation.ColorInt
import com.backbase.deferredresources.DeferredColor
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * A [DeferredColor] with a different source depending on the runtime Android SDK version.
 *
 * This is useful because different SDK levels treat colors differently in some contexts. For example, SDK 27+ can
 * support light system navigation bar colors, but lower SDKs cannot.
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if the SDK-specific source [DeferredColor]
 * instance cannot be marshalled.
 */
// Primary constructor is internal rather than private so the generated Creator can access it
@Parcelize
@Poko public class SdkIntDeferredColor internal constructor(
    private val source: @RawValue DeferredColor
) : ParcelableDeferredColor {

    /**
     * Construct a [DeferredColor] instance that resolves to a color specific to the runtime Android SDK version.
     *
     * Each constructor parameter has a default value of the next-lowest value, allowing Kotlin consumers to provide
     * only each unique source once. For example, the following would resolve to GREEN on SDKs 14-22 and BLUE on SDKs
     * 23+:
     *
     * ```kotlin
     * SdkIntDeferredColor(
     *     minSdk = DeferredColor.Constant(Color.GREEN),
     *     sdk23 = DeferredColor.Constant(Color.BLUE)
     * )
     * ```
     */
    public constructor(
        minSdk: DeferredColor,
        sdk15: DeferredColor = minSdk,
        sdk16: DeferredColor = sdk15,
        sdk17: DeferredColor = sdk16,
        sdk18: DeferredColor = sdk17,
        sdk19: DeferredColor = sdk18,
        sdk20: DeferredColor = sdk19,
        sdk21: DeferredColor = sdk20,
        sdk22: DeferredColor = sdk21,
        sdk23: DeferredColor = sdk22,
        sdk24: DeferredColor = sdk23,
        sdk25: DeferredColor = sdk24,
        sdk26: DeferredColor = sdk25,
        sdk27: DeferredColor = sdk26,
        sdk28: DeferredColor = sdk27,
        sdk29: DeferredColor = sdk28,
        sdk30: DeferredColor = sdk29,
    ) : this(
        source = when (val sdkInt = Build.VERSION.SDK_INT) {
            14 -> minSdk
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
            else -> if (sdkInt > 30) sdk30 else minSdk
        }
    )

    /**
     * Resolve the source [DeferredColor] to a [ColorInt] color for the current runtime Android SDK.
     */
    @ColorInt override fun resolve(context: Context): Int = source.resolve(context)

    /**
     * Resolve the source [DeferredColor] to a [ColorStateList] for the current runtime Android SDK.
     */
    override fun resolveToStateList(context: Context): ColorStateList = source.resolveToStateList(context)
}
