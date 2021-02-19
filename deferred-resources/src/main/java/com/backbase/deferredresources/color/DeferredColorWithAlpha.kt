package com.backbase.deferredresources.color

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils
import com.backbase.deferredresources.DeferredColor
import dev.drewhamilton.poko.Poko
import kotlin.math.roundToInt
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Create a [DeferredColor] that resolves with the given [alpha], regardless of the base color's original alpha.
 */
@JvmSynthetic
public fun DeferredColor.withAlpha(@IntRange(from = 0x00, to = 0xFF) alpha: Int): DeferredColorWithAlpha =
    DeferredColorWithAlpha(this, alpha)

/**
 * Create a [DeferredColor] that resolves with the given [alpha], regardless of the base color's original alpha.
 */
@JvmSynthetic
public fun DeferredColor.withAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): DeferredColorWithAlpha =
    DeferredColorWithAlpha(this, alpha)

/**
 * A [DeferredColor] that always resolves with a specific [alpha] value, ignoring the [base] color's alpha.
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [base] cannot be marshalled.
 */
@Parcelize
@Poko public class DeferredColorWithAlpha(
    private val base: @RawValue DeferredColor,
    @IntRange(from = 0x00, to = 0xFF) private val alpha: Int
) : ParcelableDeferredColor {

    /**
     * Convenience constructor to specify a float [alpha] value to apply on the [base] color.
     */
    public constructor(
        base: DeferredColor,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ) : this(base, (alpha * 0xFF).roundToInt())

    /**
     * Using the given [context], resolve the base color with the specified alpha applied.
     */
    @ColorInt override fun resolve(context: Context): Int {
        val baseColor = base.resolve(context)
        return ColorUtils.setAlphaComponent(baseColor, alpha)
    }

    override fun resolveToStateList(context: Context): ColorStateList {
        val baseColor = base.resolveToStateList(context)
        return baseColor.withAlpha(alpha)
    }
}
