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

package com.backbase.deferredresources.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredFormattedString

/**
 * Resolve [deferredFormattedString] with the given [formatArgs], remembering the resulting value as long as the current
 * [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(
    deferredFormattedString: DeferredFormattedString,
    vararg formatArgs: Any
): String {
    val context = LocalContext.current
    return remember(context, deferredFormattedString, *formatArgs) {
        deferredFormattedString.resolve(context, *formatArgs)
    }
}
