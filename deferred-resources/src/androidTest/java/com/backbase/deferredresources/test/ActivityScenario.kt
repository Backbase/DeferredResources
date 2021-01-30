package com.backbase.deferredresources.test

import android.app.Activity
import androidx.test.core.app.ActivityScenario

internal inline fun <reified A : Activity> launchActivity() = ActivityScenario.launch(A::class.java)
