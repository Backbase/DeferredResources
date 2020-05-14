package com.backbase.deferredresources

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Quick access to the test context.
 */
internal val context get() = InstrumentationRegistry.getInstrumentation().context

internal fun Context.compatCreateConfigurationContext(overrideConfiguration: Configuration) =
    if (Build.VERSION.SDK_INT >= 17)
        createConfigurationContext(overrideConfiguration)
    else
        ConfigurationContext(this, overrideConfiguration)

/**
 * An API < 17 alternative to [Context.createConfigurationContext]. It uses a deprecated function to copy [Resources]
 * with the [overrideConfiguration].
 *
 * This should never be copied into production code.
 */
private class ConfigurationContext(
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
