package com.backbase.deferredresources.extensions

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

internal class ViewTestActivity : Activity() {

    lateinit var view: View
        private set

    fun setView(view: View) {
        if (this::view.isInitialized)
            throw UnsupportedOperationException("The view was already set as ${this.view}")

        if (view.layoutParams == null)
            view.layoutParams = generateDefaultLayoutParams()
        val container = FrameLayout(this).apply {
            addView(view)
        }
        setContentView(container)
        this.view = view
    }

    private fun generateDefaultLayoutParams() = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
