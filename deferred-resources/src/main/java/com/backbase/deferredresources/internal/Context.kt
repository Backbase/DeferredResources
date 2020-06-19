package com.backbase.deferredresources.internal

import android.content.Context
import android.content.res.Resources
import androidx.annotation.AttrRes
import androidx.core.os.ConfigurationCompat
import java.util.Locale

/**
 * A Context's primary [Locale].
 */
internal val Context.primaryLocale: Locale
    get() = ConfigurationCompat.getLocales(resources.configuration)[0]

/**
 * Create an error message after failing to resolve an attribute. Uses [context] to attempt to get the name of
 * [resId]. Includes [attributeTypeName] if [isResolved] is true to indicate that [resId] was resolved but was the wrong
 * type.
 */
internal fun Context.createErrorMessage(
    @AttrRes resId: Int,
    attributeTypeName: String,
    isResolved: Boolean
) = try {
    val name = resources.getResourceEntryName(resId)
    val couldNotResolve = "Could not resolve attribute <$name>"
    val withContext = "with <$this>"
    if (isResolved)
        "$couldNotResolve to a $attributeTypeName $withContext"
    else
        "$couldNotResolve $withContext"
} catch (notFoundException: Resources.NotFoundException) {
    "Attribute <$resId> could not be found with <$this>"
}
