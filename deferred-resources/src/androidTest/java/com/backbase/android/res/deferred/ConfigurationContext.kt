package com.backbase.android.res.deferred

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources

/**
 * An API < 17 alternative to [Context.createConfigurationContext]. It uses a deprecated function to copy [Resources]
 * with the [overrideConfiguration].
 *
 * This should never be copied into production code.
 */
internal class ConfigurationContext(
    base: Context,
    private val overrideConfiguration: Configuration
) : ContextWrapper(base) {

    private var copiedResources: Resources? = null

    override fun getResources(): Resources {
        return copiedResources ?: synchronized<Resources>(this) {
            var copiedResources = this.copiedResources
            if (copiedResources == null) {
                val originalResources = baseContext.resources

                @Suppress("DEPRECATION")
                copiedResources = Resources(
                    originalResources.assets, originalResources.displayMetrics,
                    overrideConfiguration
                )
            }
            copiedResources
        }
    }
}
