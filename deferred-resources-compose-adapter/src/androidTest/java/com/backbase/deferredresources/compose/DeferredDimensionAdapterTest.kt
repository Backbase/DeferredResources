package com.backbase.deferredresources.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredDimensionAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun constructWithComposeDp_resolvesExpectedValue() {
        val deferred = DeferredDimension.Constant(16.dp)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedDp(deferred),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(16.dp)
    }

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredDimension.Resource(R.dimen.testDimen)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedDp(deferred),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(0.25.dp)
    }
}
