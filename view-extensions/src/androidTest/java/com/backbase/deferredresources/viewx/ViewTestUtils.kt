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

package com.backbase.deferredresources.viewx

import android.content.Context
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Convenience for running a test on a specific type of View.
 */
internal inline fun <reified V : View> onView(
    test: V.() -> Unit
) {
    val view = construct<V>()
    view.test()
}

/**
 * Convenience for running a test on a specific type of View.
 */
internal inline fun <reified V : View> onViewInActivity(
    noinline afterIdleSync: (V.() -> Unit)? = null,
    crossinline test: V.() -> Unit
) {
    val scenario = ActivityScenario.launch(ViewTestActivity::class.java)
    scenario.onActivity {
        val view = construct<V>()
        it.setView(view)
        view.test()
    }

    if (afterIdleSync != null) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        scenario.onActivity { it.view<V>().afterIdleSync() }
    }
}

private inline fun <reified V : View> ViewTestActivity.view(): V = view as V

private inline fun <reified V : View> construct() = V::class.java
    .getDeclaredConstructor(Context::class.java)
    .newInstance(InstrumentationRegistry.getInstrumentation().context)
