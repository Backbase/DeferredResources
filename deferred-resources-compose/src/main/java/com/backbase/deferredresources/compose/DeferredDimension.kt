package com.backbase.deferredresources.compose

import android.content.Context
import androidx.annotation.Px
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.dimension.ParcelableDeferredDimension
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize

/**
 * Resolve the [DeferredDimension] to an exact [Dp] value using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredDimension.resolve(): Dp = when (this) {
    is ComposeDeferredDimension -> value
    else -> with(LocalDensity.current) {
        resolveExact(LocalContext.current).toDp()
    }
}

/**
 * Resolve the [DeferredDimension] to an integer [Dp] value for use as a size, using the current composition-local
 * Context. The exact value is rounded, and non-zero exact values are ensured to be at least one pixel in size.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredDimension.resolveAsSize(): Dp = with(LocalDensity.current) {
    resolveAsSize(LocalContext.current).toDp()
}

/**
 * Resolve the [DeferredDimension] to an integer [Dp] value using the current composition-local Context. The exact value
 * is truncated to an integer.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredDimension.resolveAsOffset(): Dp = with(LocalDensity.current) {
    resolveAsOffset(LocalContext.current).toDp()
}

@Parcelize
@Poko public class ComposeDeferredDimension(
    internal val value: Dp,
) : ParcelableDeferredDimension {

    @Px override fun resolveExact(context: Context): Float = with(Density(context)) {
        value.toPx()
    }

    @Px override fun resolveAsSize(context: Context): Int = with(Density(context)) {
        val roundedPxValue = value.roundToPx()
        when {
            roundedPxValue != 0 -> roundedPxValue
            value.value == 0f -> 0
            value.value > 0f -> 1
            else -> -1
        }
    }

    @Px override fun resolveAsOffset(context: Context): Int = with(Density(context)) {
        value.toPx().toInt()
    }
}
