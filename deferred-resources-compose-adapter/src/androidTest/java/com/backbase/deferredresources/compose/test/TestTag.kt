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

package com.backbase.deferredresources.compose.test

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * A convenient tag string to use for test nodes.
 */
internal const val TestTag = "deferred-resources-compose-adapter-test-tag"

/**
 * A convenience for [Modifier.testTag] ([TestTag]).
 */
internal val TestTagModifier = Modifier.testTag(TestTag)
