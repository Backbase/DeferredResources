package com.backbase.deferredresources.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalDeferredResourcesComposeSupport::class)
internal class DeferredColorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun constructWithComposeColor_resolvesExpectedValue() {
        val deferred = DeferredColor.Constant(Color(0xff00ff00))
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(Color.Green)
    }

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredColor.Resource(R.color.blue)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(Color.Blue)
    }
}
