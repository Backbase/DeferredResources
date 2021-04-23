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

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.os.ConfigurationCompat
import java.util.Locale

/**
 * A Context's primary [Locale].
 */
internal val Context.primaryLocale: Locale
    get() = ConfigurationCompat.getLocales(resources.configuration)[0]

/**
 * Use a [Context] to resolve a [resId] attribute. [toTypeSafeResult] can assume the [TypedValue] has been filled and
 * has been validated as having one of the [expectedTypes].
 *
 * [reusedTypedValue] is filled with the resolved attribute information and is cleared after [toTypeSafeResult] returns.
 * As the name suggests, the same [TypedValue] instance can then be reused for multiple calls to this function.
 *
 * Throws [IllegalArgumentException] if the attribute cannot be resolved or if it is resolved as a type other than
 * [expectedTypes]. [attributeTypeName] is used to create a helpful error message.
 */
internal inline fun <T> Context.resolveAttribute(
    @AttrRes resId: Int,
    attributeTypeName: String,
    reusedTypedValue: TypedValue,
    vararg expectedTypes: Int,
    resolveRefs: Boolean = true,
    toTypeSafeResult: TypedValue.() -> T
): T {
    try {
        val isResolved = theme.resolveAttribute(resId, reusedTypedValue, resolveRefs)
        if (isResolved && expectedTypes.contains(reusedTypedValue.type))
            return reusedTypedValue.toTypeSafeResult()
        else
            throw IllegalArgumentException(createErrorMessage(resId, attributeTypeName, isResolved))
    } finally {
        // Clear for re-use:
        reusedTypedValue.setTo(EMPTY_TYPED_VALUE)
    }
}

/**
 * Create an error message after failing to resolve a [resId] attribute. Includes [attributeTypeName] if [isResolved] is
 * true to indicate that [resId] was resolved but was the wrong type.
 */
private fun Context.createErrorMessage(
    @AttrRes resId: Int,
    attributeTypeName: String,
    isResolved: Boolean
) = try {
    val name = resources.getResourceEntryName(resId)
    val couldNotResolve = "Could not resolve attribute <$name>"
    val withContext = "with context <$this>"
    if (isResolved)
        "$couldNotResolve to a $attributeTypeName $withContext"
    else
        "$couldNotResolve $withContext"
} catch (notFoundException: Resources.NotFoundException) {
    "Attribute <$resId> could not be found with context <$this>"
}

/**
 * An empty [TypedValue] used to reset "Attribute" implementations' internal [TypedValue]s after resolution.
 *
 * Mutating this value is incorrect behavior and may cause unexpected bugs.
 */
private val EMPTY_TYPED_VALUE: TypedValue = TypedValue()
