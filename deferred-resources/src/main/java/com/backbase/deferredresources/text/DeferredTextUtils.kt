/*
 * Copyright 2020 Backbase R&D B.V.
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

@file:JvmName("DeferredTextUtils")

package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredText

/**
 * Resolve a [DeferredText] to a string rather than a CharSequence by calling [toString] on the resolved value. Any
 * styling information from the original CharSequence will typically be removed from the resolved string.
 */
public fun DeferredText.resolveToString(context: Context): String = resolve(context).toString()
