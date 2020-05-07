package com.backbase.android.res.deferred

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before

abstract class SpecificLocaleTest {

    protected var context: Context = InstrumentationRegistry.getInstrumentation().context
        private set

    /**
     * Ensure the test context is the normal instrumentation context by default.
     */
    @Before fun resetTestContext() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    /**
     * Set the test Context to one with a specific [language] locale.
     */
    protected fun setTestLanguage(language: String) {
        val newConfiguration = Configuration(context.resources.configuration).apply {
            setLocales(LocaleListCompat.forLanguageTags(language))
        }
        context = context.createConfigurationContext(newConfiguration)
    }

    private fun Configuration.setLocales(locales: LocaleListCompat) = when {
        Build.VERSION.SDK_INT >= 24 ->
            setLocales(locales.unwrap() as LocaleList)
        Build.VERSION.SDK_INT >= 17 ->
            setLocale(locales[0])
        else ->
            @Suppress("DEPRECATION")
            locale = locales[0]
    }
}
