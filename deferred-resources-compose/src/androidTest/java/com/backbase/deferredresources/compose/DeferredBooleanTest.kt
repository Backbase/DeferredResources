package com.backbase.deferredresources.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.backbase.deferredresources.DeferredBoolean
import com.backbase.deferredresources.compose.test.R
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalDeferredResourcesComposeSupport::class)
internal class DeferredBooleanTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredBoolean.Resource(R.bool.testBool)
        composeTestRule.setContent {
            MaterialTheme {
                Text(text = deferred.resolve().toString())
            }
        }

        composeTestRule.onNodeWithText("true").assertExists()
    }
}
