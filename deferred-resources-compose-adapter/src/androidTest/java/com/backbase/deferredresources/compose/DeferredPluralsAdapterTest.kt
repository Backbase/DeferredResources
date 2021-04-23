package com.backbase.deferredresources.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
import com.backbase.deferredresources.DeferredPlurals
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredPluralsAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun constructWithAnnotatedString_resolvesAnnotatedString() {
        val oneValue = AnnotatedString(
            text = "One",
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
        )
        val otherValue = AnnotatedString(
            text = "Other",
            paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
        )
        val deferred = DeferredPlurals.Constant(
            one = oneValue,
            other = otherValue,
        )
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(1),
                modifier = Modifier.testTag("oneValue"),
            )
            GenericValueNode(
                value = deferred.resolve(2),
                modifier = Modifier.testTag("otherValue"),
            )
        }

        composeTestRule.onNodeWithTag("oneValue").assertGenericValueEquals(oneValue)
        composeTestRule.onNodeWithTag("otherValue").assertGenericValueEquals(otherValue)
    }

    @Test fun resolve_withLocalContext_returnsExpectedValue() {
        val deferred = DeferredPlurals.Resource(R.plurals.plainPlurals)
        composeTestRule.setContent {
            GenericValueNode(
                value = deferred.resolve(1),
                modifier = Modifier.testTag("oneValue"),
            )
            GenericValueNode(
                value = deferred.resolve(9),
                modifier = Modifier.testTag("otherValue"),
            )
        }

        composeTestRule.onNodeWithTag("oneValue").assertGenericValueEquals(AnnotatedString("Car"))
        composeTestRule.onNodeWithTag("otherValue").assertGenericValueEquals(AnnotatedString("Cars"))
    }
}
