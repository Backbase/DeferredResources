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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.backbase.deferredresources.DeferredDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Resolve a [deferredDrawable] into a Compose [Painter], remembering the resulting painter as long as the current
 * [LocalContext] and [deferredDrawable] don't change.
 *
 * Note: If used in the material Icon composable, [androidx.compose.ui.graphics.Color.Unspecified] must be provided as
 * the tint color in order to retain the original Drawable's tint.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedPainter(deferredDrawable: DeferredDrawable): Painter {
    val context = LocalContext.current
    val drawable = remember(deferredDrawable) {
        deferredDrawable.resolve(context)
    }

    return rememberDrawablePainter(drawable)
}
