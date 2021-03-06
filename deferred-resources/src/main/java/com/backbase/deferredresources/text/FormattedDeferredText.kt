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
import com.backbase.deferredresources.DeferredFormattedString
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.poko.Poko
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Convert a [DeferredFormattedString] to a normal [DeferredText] by providing [formatArgs] to be used when resolved.
 */
@JvmSynthetic public fun DeferredFormattedString.withFormatArgs(vararg formatArgs: Any): FormattedDeferredText =
    FormattedDeferredText(wrapped = this, formatArgs = formatArgs)

/**
 * Convert a [DeferredFormattedString] to a normal [DeferredText] by providing [formatArgs] to be used when resolved.
 */
@Suppress("unused")
@Deprecated("Covariant return type introduced", level = DeprecationLevel.HIDDEN)
// Unused generic is added to allow return-type overload
@JvmSynthetic public fun <T> DeferredFormattedString.withFormatArgs(vararg formatArgs: Any): DeferredText =
    FormattedDeferredText(wrapped = this, formatArgs = formatArgs)

/**
 * A [DeferredText] implementation that wraps a [DeferredFormattedString] along with its [formatArgs]. Designed for use
 * cases where the format args are determined at the declaring site rather than the resolving site.
 *
 * If the format args are to be determined at the resolving site, stick with [DeferredFormattedString].
 *
 * This class implements [android.os.Parcelable]. It will throw at runtime if [wrapped] or any of [formatArgs] cannot be
 * marshalled.
 */
// Primary constructor is internal rather than private so the generated Creator can access it
@Parcelize
@Poko public class FormattedDeferredText internal constructor(
    private val wrapped: @RawValue DeferredFormattedString,
    private val formatArgs: @RawValue List<Any>,
) : ParcelableDeferredText {

    /**
     * Initialize with the given [wrapped] [DeferredFormattedString] and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    public constructor(
        wrapped: DeferredFormattedString,
        vararg formatArgs: Any
    ) : this(wrapped, formatArgs.toList())

    /**
     * Resolve [wrapped] with [formatArgs] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = wrapped.resolve(context, *formatArgs.toTypedArray())
}
