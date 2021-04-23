package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
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
 * Resolve [deferredDimension] to a [Dp] value, remembering the resulting value as long as the current [LocalContext]
 * doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedDp(deferredDimension: DeferredDimension): Dp {
    val context = LocalContext.current
    return remember(context, deferredDimension) {
        with(Density(context)) {
            deferredDimension.resolveExact(context).toDp()
        }
    }
}
