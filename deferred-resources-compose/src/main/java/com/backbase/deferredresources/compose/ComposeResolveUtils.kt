@file:JvmName("ComposeResolveUtils")

package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import com.backbase.deferredresources.DeferredBoolean
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredFormattedString
import com.backbase.deferredresources.DeferredInteger
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.text.resolveToString

/**
 * Resolve the [DeferredBoolean] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredBoolean.resolve(): Boolean = resolve(AmbientContext.current)

/**
 * Resolve the [DeferredInteger] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredInteger.resolve(): Int = resolve(AmbientContext.current)

/**
 * Resolve the [DeferredFormattedPlurals] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedPlurals.resolve(
    quantity: Int,
    vararg formatArgs: Any = arrayOf(quantity)
): String = resolve(AmbientContext.current, quantity, *formatArgs)

/**
 * Resolve the [DeferredFormattedString] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredFormattedString.resolve(vararg formatArgs: Any): String =
    resolve(AmbientContext.current, *formatArgs)

/**
 * Resolve the [DeferredPlurals] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredPlurals.resolve(quantity: Int): CharSequence =
    resolve(AmbientContext.current, quantity)

/**
 * Resolve the [DeferredText] using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredText.resolve(): CharSequence = resolve(AmbientContext.current)

/**
 * Resolve the [DeferredText] to a String using the current ambient Context.
 */
@ExperimentalDeferredResourcesComposeSupport
@Composable public fun DeferredText.resolveToString(): CharSequence = resolveToString(AmbientContext.current)
