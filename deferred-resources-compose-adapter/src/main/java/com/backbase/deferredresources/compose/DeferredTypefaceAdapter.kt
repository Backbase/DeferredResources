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
import androidx.compose.ui.text.font.FontFamily
import com.backbase.deferredresources.DeferredTypeface

/**
 * Resolve a [deferredTypeface] to a Compose [FontFamily], remembering the resulting font family instance as long as the
 * current [LocalContext] and [deferredTypeface] don't change.
 *
 * If the given [deferredTypeface] resolves to a null typeface, returns null.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedFontFamily(deferredTypeface: DeferredTypeface): FontFamily? {
    val context = LocalContext.current
    val typeface = remember(deferredTypeface) {
        deferredTypeface.resolve(context)
    }

    return remember(typeface) {
        if (typeface == null) null else FontFamily(typeface)
    }
}
