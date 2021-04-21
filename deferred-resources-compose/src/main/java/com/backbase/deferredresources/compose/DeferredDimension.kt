package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.backbase.deferredresources.DeferredDimension

/**
 * Instantiate a [DeferredDimension.DpConstant] with a Compose [Dp] [value].
 */
@ExperimentalDeferredResourcesComposeSupport
@Suppress("FunctionName") // Factory
public fun DeferredDimension.Companion.DpConstant(value: Dp): DeferredDimension.DpConstant =
    DeferredDimension.DpConstant(dpValue = value.value)

/**
 * Resolve the [DeferredDimension] to an exact [Dp] value using the current composition-local Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredDimension.resolve(): Dp = with(LocalDensity.current) {
    resolveExact(LocalContext.current).toDp()
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
