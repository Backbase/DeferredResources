package com.backbase.deferredresources.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.backbase.deferredresources.DeferredInteger
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalDeferredResourcesComposeSupport::class)
internal class DeferredIntegerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredInteger.Resource(R.integer.five)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(5)
    }
}
