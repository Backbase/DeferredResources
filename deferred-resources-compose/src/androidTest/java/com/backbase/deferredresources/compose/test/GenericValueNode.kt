package com.backbase.deferredresources.compose.test

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/**
 * A Compose node which holds a single [value], which can be asserted with [assertGenericValueEquals].
 */
@Suppress("TestFunctionName") // Composable
@Composable internal fun <T> GenericValueNode(
    value: T,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier.semantics(
        properties = {
            set(GenericValue, value)
        }
    )
)

/**
 * Assert that the node is a [GenericValueNode] with the given [value].
 */
internal fun <T> SemanticsNodeInteraction.assertGenericValueEquals(value: T) =
    assert(SemanticsMatcher.expectValue(GenericValue, value))

private val GenericValue = SemanticsPropertyKey<Any?>("GenericValue") { parentNode, _ -> parentNode }
