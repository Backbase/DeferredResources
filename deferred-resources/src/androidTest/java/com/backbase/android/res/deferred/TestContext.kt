package com.backbase.android.res.deferred

import android.content.Context
import android.content.res.Configuration
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
