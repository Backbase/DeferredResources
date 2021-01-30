@file:JvmName("TestContext")

package com.backbase.deferredresources.test

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.view.ContextThemeWrapper
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Quick access to the test context.
 */
internal val context: Context
    get() = InstrumentationRegistry.getInstrumentation().context

/**
 * Quick access to the test context wrapped in an AppCompat theme.
 */
@Suppress("TestFunctionName") // Factory
internal fun AppCompatContext(
    light: Boolean = false
): Context = ContextThemeWrapper(
    context,
    if (light) R.style.TestTheme_Light else R.style.TestTheme
)

//region Configuration
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
//endregion

//region Restricted
/**
 * Creates a Context that always returns true from [Context.isRestricted]. If the receiver is already restricted,
 * returns itself.
 */
internal fun Context.createRestrictedContext() = if (isRestricted) this else object : ContextWrapper(this) {
    override fun isRestricted() = true
}
//endregion
