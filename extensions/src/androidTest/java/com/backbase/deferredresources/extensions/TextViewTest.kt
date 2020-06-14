package com.backbase.deferredresources.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.DeferredTypeface
import com.backbase.deferredresources.extensions.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assume.assumeFalse
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

    @Test fun setTypeface_displaysResolvedTypeface() = onView { view ->
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        view.setTypeface(deferred)

        assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
        if (Build.VERSION.SDK_INT >= 28)
            assertThat(view.typeface.weight).isEqualTo(300)
        assertThat(view.typeface.style).isEqualTo(Typeface.ITALIC)
    }

    @Test fun setTypeface_withStyle_displaysResolvedTypefaceWithStyle() = onView { view ->
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        view.setTypeface(deferred, Typeface.BOLD)

        assumeFalse("https://issuetracker.google.com/issues/156853883", Build.VERSION.SDK_INT == 29)
        if (Build.VERSION.SDK_INT >= 28)
            assertThat(view.typeface.weight).isEqualTo(600)
        assertThat(view.typeface.style).isEqualTo(Typeface.BOLD)
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
