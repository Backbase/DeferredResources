package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.drawable.asDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ViewTest {

    @Test fun setBackground_setsResolvedBackground() = onView {
        val deferred = DeferredColor.Constant(Color.BLUE).asDrawable()
        setBackground(deferred)

        assertThat(background).isEqualTo(deferred.resolve(context))
    }

    private fun onView(
        afterIdleSync: (View.() -> Unit)? = null,
        test: View.() -> Unit
    ) {
        val scenario = ActivityScenario.launch(Activity::class.java)
        scenario.onActivity { it.view.test() }

        if (afterIdleSync != null) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.onActivity { it.view.afterIdleSync() }
        }
    }

    class Activity : android.app.Activity() {

        lateinit var view: View
            private set

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            view = View(this).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val container = FrameLayout(this).apply {
                addView(view)
            }
            setContentView(container)
        }
    }
}
