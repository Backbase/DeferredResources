package com.backbase.deferredresources.extensions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.backbase.deferredresources.DeferredText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Test

class TextViewTest {

    @Test fun setText_displaysResolvedText() = onActivity(
        execute = {
            val deferred = DeferredText.Constant("Test")
            view.setText(deferred)
        },
        assert = {
            check(matches(withText("Test")))
        }
    )

    private fun onActivity(execute: Activity.() -> Unit, assert: ViewInteraction.() -> Unit) {
        val scenario = ActivityScenario.launch(Activity::class.java)
        var matcher: Matcher<View>? = null
        scenario.onActivity { activity ->
            matcher = object : BoundedMatcher<View, TextView>(TextView::class.java) {
                override fun matchesSafely(item: TextView) = item === activity.view
                override fun describeTo(description: Description) {
                    description.appendText("is ").appendValue(activity.view)
                }
            }
            activity.execute()
        }
        onView(matcher!!).assert()
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
