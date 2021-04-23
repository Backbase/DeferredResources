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

@file:JvmName("DeferredResourcesImageViewUtils")

package com.backbase.deferredresources.viewx

import android.graphics.PorterDuff
import android.widget.ImageView
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable

/**
 * Resolves [deferredDrawable] and sets the resolved drawable as the content of this ImageView.
 */
public fun ImageView.setImageDrawable(deferredDrawable: DeferredDrawable): Unit =
    setImageDrawable(deferredDrawable.resolve(context))

/**
 * Resolve [deferredColor] and set the resolved color as a tinting option for the image. Assumes
 * [PorterDuff.Mode.SRC_ATOP] blending mode.
 */
public fun ImageView.setColorFilter(deferredColor: DeferredColor): Unit =
    setColorFilter(deferredColor.resolve(context))

/**
 * Resolve [deferredColor] and set the resolved color as a tinting option for the image and the given blending [mode].
 */
public fun ImageView.setColorFilter(deferredColor: DeferredColor, mode: PorterDuff.Mode): Unit =
    setColorFilter(deferredColor.resolve(context), mode)
