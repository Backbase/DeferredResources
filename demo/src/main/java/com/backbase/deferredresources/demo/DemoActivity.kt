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

package com.backbase.deferredresources.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.backbase.deferredresources.demo.databinding.DemoBinding
import com.backbase.deferredresources.demo.core.SamplesViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DemoActivity : AppCompatActivity() {

    private val viewModel: SamplesViewModel by samplesViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (binding.pager.adapter == null)
            binding.pager.adapter = DemoPagerAdapter(viewModel)

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = (binding.pager.adapter as DemoPagerAdapter).getPageName(position).resolve(this)
        }.attach()
    }
}
