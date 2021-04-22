package com.backbase.deferredresources.compose

import androidx.compose.material.Text
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.backbase.deferredresources.DeferredTypeface
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredTypefaceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun rememberResolvedPainter_paintsDrawableAndHasExpectedSize() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)

        composeTestRule.setContent {
            val fontFamily = rememberResolvedFontFamily(deferred)
            Text(
                text = "DeferredTypeface test",
                modifier = TestTagModifier,
                fontFamily = fontFamily,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertTextEquals("DeferredTypeface test")
        // TODO: Assert something about the typeface here. We don't have public access to any properties of FontFamily.
    }
}
