package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.backbase.deferredresources.DeferredDimension

/**
 * Instantiate a [DeferredDimension.Constant] with a Compose [Dp] [value].
 */
@ExperimentalComposeAdapter
@Suppress("FunctionName") // Factory
public fun DeferredDimension.Companion.Constant(value: Dp): DeferredDimension.Constant =
    DeferredDimension.Constant(value = value.value, unit = DeferredDimension.Constant.Unit.DP)

/**
 * Resolve the [DeferredDimension] to an exact [Dp] value using the current composition-local Context.
 */
@ExperimentalComposeAdapter
@Composable public fun DeferredDimension.resolve(): Dp = with(LocalDensity.current) {
    resolveExact(LocalContext.current).toDp()
}

/**
 * Resolve the [DeferredDimension] to an integer [Dp] value for use as a size, using the current composition-local
 * Context. The exact value is rounded, and non-zero exact values are ensured to be at least one pixel in size.
 */
@ExperimentalComposeAdapter
@Composable public fun DeferredDimension.resolveAsSize(): Dp = with(LocalDensity.current) {
    resolveAsSize(LocalContext.current).toDp()
}

/**
 * Resolve the [DeferredDimension] to an integer [Dp] value using the current composition-local Context. The exact value
 * is truncated to an integer.
 */
@ExperimentalComposeAdapter
@Composable public fun DeferredDimension.resolveAsOffset(): Dp = with(LocalDensity.current) {
    resolveAsOffset(LocalContext.current).toDp()
}
