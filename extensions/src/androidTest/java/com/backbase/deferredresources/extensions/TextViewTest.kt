package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredText
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TextViewTest {

    @Test fun setText_displaysResolvedText() = onView { view ->
        val deferred = DeferredText.Constant("Deferred")
        view.setText(deferred)

        assertThat(view.text).isEqualTo("Deferred")
    }

    @Test fun setText_withBufferType_displaysResolvedText() = onView { view ->
        assertThat(view.editableText).isNull()

        val deferred = DeferredText.Constant("Deferred")
        view.setText(deferred, TextView.BufferType.EDITABLE)

        assertThat(view.text.toString()).isEqualTo("Deferred")
        assertThat(view.editableText).isNotNull()
        assertThat(view.editableText.toString()).isEqualTo("Deferred")
    }

    @Test fun setHint_displaysResolvedHint() = onView { view ->
        val deferred = DeferredText.Constant("Deferred")
        view.setHint(deferred)

        assertThat(view.hint).isEqualTo("Deferred")
    }

    @Test fun setTextColor_displaysResolvedColor() = onView { view ->
        val deferred = DeferredColor.Constant(Color.GREEN)
        view.setTextColor(deferred)

        assertThat(view.currentTextColor).isEqualTo(Color.GREEN)
    }

    @Test fun setHintTextColor_displaysResolvedColor() = onView { view ->
        val deferred = DeferredColor.Constant(Color.LTGRAY)
        view.setHintTextColor(deferred)

        assertThat(view.currentHintTextColor).isEqualTo(Color.LTGRAY)
    }

    private fun onView(test: (TextView) -> Unit) {
        val scenario = ActivityScenario.launch(Activity::class.java)
        scenario.onActivity { test(it.view) }
    }

    class Activity : android.app.Activity() {

        lateinit var view: TextView
            private set

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            view = TextView(this)
            setContentView(view)
        }
    }
}
