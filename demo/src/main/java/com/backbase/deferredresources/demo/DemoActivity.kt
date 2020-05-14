package com.backbase.deferredresources.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.backbase.deferredresources.demo.databinding.DemoBinding
import com.google.android.material.tabs.TabLayoutMediator

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (binding.pager.adapter == null)
            binding.pager.adapter = DemoPagerAdapter()

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = (binding.pager.adapter as DemoPagerAdapter).getPageName(position)
        }.attach()
    }
}
