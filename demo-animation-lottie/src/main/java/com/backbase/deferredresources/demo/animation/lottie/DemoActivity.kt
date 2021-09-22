/*
 * Copyright 2021 Backbase R&D B.V.
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

package com.backbase.deferredresources.demo.animation.lottie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieDrawable
import com.backbase.deferredresources.animation.lottie.DeferredLottieDrawable
import com.backbase.deferredresources.demo.animation.lottie.databinding.DemoBinding

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.drawableView.apply {
            val drawable = DeferredLottieDrawable.Resource(R.raw.demo_lottie_file) {
                playAnimation()
                repeatCount = 100
            }.resolve(context)

            setImageDrawable(drawable)
        }
    }
}
