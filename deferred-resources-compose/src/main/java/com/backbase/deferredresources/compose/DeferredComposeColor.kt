package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import com.backbase.deferredresources.DeferredColor
import dev.drewhamilton.extracare.DataApi

/**
 * Wrap a legacy [DeferredColor] in a [DeferredComposeColor] for use in Compose UI.
 */
@ExperimentalDeferredResourcesComposeSupport
public fun DeferredColor.forCompose(): DeferredComposeColor = DeferredComposeColor.OfDeferredColor(this)

/**
 * A wrapper for resolving a Compose [Color] on demand.
 */
@ExperimentalDeferredResourcesComposeSupport
public interface DeferredComposeColor {

    /**
     * Resolve the [Color].
     */
    @Composable public fun resolve(): Color

    /**
     * A wrapper for a constant [Color] value.
     */
    @ExperimentalDeferredResourcesComposeSupport
    @DataApi public class Constant(
        private val color: Color
    ) : DeferredComposeColor {
        @Composable override fun resolve(): Color = color
    }

    /**
     * A wrapper for a non-Compose [deferredColor] instance, to be resolved to a Compose [Color].
     */
    @ExperimentalDeferredResourcesComposeSupport
    @DataApi public class OfDeferredColor(
        private val deferredColor: DeferredColor
    ) : DeferredComposeColor {
        @Composable override fun resolve(): Color = Color(deferredColor.resolve(AmbientContext.current))
    }
}
