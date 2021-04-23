/*
 * Copyright 2021 Backbase R&D B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
