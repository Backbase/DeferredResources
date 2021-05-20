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
