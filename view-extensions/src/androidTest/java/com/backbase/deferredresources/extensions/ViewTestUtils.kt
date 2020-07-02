package com.backbase.deferredresources.extensions

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
