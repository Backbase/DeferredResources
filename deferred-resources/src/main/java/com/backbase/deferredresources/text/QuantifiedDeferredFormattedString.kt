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

package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.DeferredFormattedString
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredFormattedString] by providing a [quantity] to be used when
 * resolved. Format arguments must still be provided when resolved.
 */
@JvmSynthetic public fun DeferredFormattedPlurals.withQuantity(quantity: Int): QuantifiedDeferredFormattedString =
    QuantifiedDeferredFormattedString(wrapped = this, quantity = quantity)

/**
 * Convert a [DeferredFormattedPlurals] to a [DeferredFormattedString] by providing a [quantity] to be used when
 * resolved. Format arguments must still be provided when resolved.
 */
@Suppress("unused")
@Deprecated(
    message = "Covariant return type introduced",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith(
        "QuantifiedDeferredFormattedString(wrapped = this, quantity = quantity)",
        "com.backbase.deferredresources.text.QuantifiedDeferredFormattedString"
    )
)
// Unused generic is added to allow return-type overload
@JvmSynthetic public fun <T> DeferredFormattedPlurals.withQuantity(quantity: Int): DeferredFormattedString =
    QuantifiedDeferredFormattedString(wrapped = this, quantity = quantity)

/**
 * A [DeferredFormattedString] implementation that wraps a [DeferredFormattedPlurals] along with its [quantity].
 * Designed for use cases where the quantity is determined at the declaring site while the format args are determined at
 * the resolving site.
 *
 * If the quantity is to be determined at the resolving site, stick with [DeferredFormattedPlurals]. If the format args
 * are to be determined at the declaring site, see [QuantifiedFormattedDeferredText].
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [wrapped] cannot be marshalled.
 */
@Parcelize
@Poko public class QuantifiedDeferredFormattedString(
    private val wrapped: @RawValue DeferredFormattedPlurals,
    private val quantity: Int
) : ParcelableDeferredFormattedString {
    /**
     * Resolve [wrapped] with [quantity] and [formatArgs] using the given [context].
     */
    override fun resolve(context: Context, vararg formatArgs: Any): String =
        wrapped.resolve(context, quantity, *formatArgs)
}
