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

package com.backbase.deferredresources.internal

import kotlin.math.roundToInt

/**
 * Convert a float to an integer value for use as a size. The exact value is rounded, and non-zero exact values always
 * result in a size of at least one.
 */
internal fun Float.toSize(): Int {
    val rounded = roundToInt()
    return when {
        rounded != 0 -> rounded
        this == 0f -> 0
        this > 0f -> 1
        else -> -1
    }
}
