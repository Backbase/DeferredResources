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

package com.backbase.deferredresources.viewx

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.drawable.asDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Assume.assumeFalse
import org.junit.Test

internal class ImageViewTest {

    @Test fun setImageDrawable_setsResolvedDrawable() =
        onView<ImageView> {
            val deferred = DeferredColor.Constant(Color.YELLOW).asDrawable()
            setImageDrawable(deferred)

            val imageDrawable = drawable as ColorDrawable
            assertThat(imageDrawable.color).isEqualTo(Color.YELLOW)
        }

    @Test fun setColorFilter_withoutMode_setsColorInAtopMode() =
        onView<ImageView> {
            val deferred = DeferredColor.Constant(Color.YELLOW)
            setColorFilter(deferred)

            assumeFalse("Cannot verify color filter on API < 21", Build.VERSION.SDK_INT < 21)
            val colorFilter = colorFilter as PorterDuffColorFilter
            assertThat(colorFilter.getMode()).isEqualTo(PorterDuff.Mode.SRC_ATOP)
            assertThat(colorFilter.getColor()).isEqualTo(Color.YELLOW)
        }

    @Test fun setColorFilter_withMode_setsColorInGivenMode() =
        onView<ImageView> {
            val deferred = DeferredColor.Constant(Color.GREEN)
            setColorFilter(deferred, PorterDuff.Mode.ADD)

            assumeFalse("Cannot verify color filter on API < 21", Build.VERSION.SDK_INT < 21)
            val colorFilter = colorFilter as PorterDuffColorFilter
            assertThat(colorFilter.getMode()).isEqualTo(PorterDuff.Mode.ADD)
            assertThat(colorFilter.getColor()).isEqualTo(Color.GREEN)
        }

    @RequiresApi(21)
    private fun PorterDuffColorFilter.getMode(): PorterDuff.Mode {
        return PorterDuffColorFilter::class.java.getDeclaredMethod("getMode").invoke(this) as PorterDuff.Mode
    }

    @RequiresApi(21)
    @ColorInt private fun PorterDuffColorFilter.getColor(): Int {
        return PorterDuffColorFilter::class.java.getDeclaredMethod("getColor").invoke(this) as Int
    }
}
