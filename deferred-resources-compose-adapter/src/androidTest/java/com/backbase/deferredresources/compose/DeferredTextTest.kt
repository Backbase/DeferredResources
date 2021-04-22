package com.backbase.deferredresources.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun constructWithAnnotatedString_resolvesAnnotatedString() {
        val originalValue = AnnotatedString(
            text = "Test string",
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
        )
        val deferred = DeferredText.Constant(originalValue)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(originalValue)
    }

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredText.Resource(R.string.plainString)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(AnnotatedString("A string"))
    }

    @Test fun resolveToString_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredText.Resource(R.string.plainString)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolveToString(),
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals("A string")
    }
}
