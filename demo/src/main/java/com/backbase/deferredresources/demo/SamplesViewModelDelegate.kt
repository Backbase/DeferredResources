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

package com.backbase.deferredresources.demo

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.backbase.deferredresources.demo.core.SamplesViewModel

fun ComponentActivity.samplesViewModel(): Lazy<SamplesViewModel> {
    val lazyWrapper = viewModels<SamplesViewModelWrapper>()
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        lazyWrapper.value.value
    }
}

class SamplesViewModelWrapper: ViewModel() {
    val value = SamplesViewModel()
}
