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

package com.backbase.deferredresources.test

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.core.os.LocaleListCompat
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before

internal abstract class SpecificLocaleTest {

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
        context = context.compatCreateConfigurationContext(newConfiguration)
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
