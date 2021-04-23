package com.backbase.deferredresources.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.backbase.deferredresources.DeferredFormattedPlurals
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredFormattedPluralsAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun resolve_withQuantity_returnsExpectedValue() {
        val deferred = DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedString(deferred, 1),
                modifier = Modifier.testTag("oneValue"),
            )
            GenericValueNode(
                value = rememberResolvedString(deferred, 9),
                modifier = Modifier.testTag("otherValue"),
            )
        }

        composeTestRule.onNodeWithTag("oneValue").assertGenericValueEquals("1 bear")
        composeTestRule.onNodeWithTag("otherValue").assertGenericValueEquals("9 bears")
    }

    @Test fun resolve_withQuantityAndFormatArgs_returnsExpectedValue() {
        val deferred = DeferredFormattedPlurals.Resource(R.plurals.formattedPlurals)
        composeTestRule.setContent {
            GenericValueNode(
                value = rememberResolvedString(deferred, 1, 9),
                modifier = Modifier.testTag("oneValue"),
            )
            GenericValueNode(
                value = rememberResolvedString(deferred, 9, 1),
                modifier = Modifier.testTag("otherValue"),
            )
        }

        composeTestRule.onNodeWithTag("oneValue").assertGenericValueEquals("9 bear")
        composeTestRule.onNodeWithTag("otherValue").assertGenericValueEquals("1 bears")
    }
}
