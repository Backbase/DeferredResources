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
