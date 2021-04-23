package com.backbase.deferredresources.compose

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.compose.test.GenericValueNode
import com.backbase.deferredresources.compose.test.R
import com.backbase.deferredresources.compose.test.TestTag
import com.backbase.deferredresources.compose.test.TestTagModifier
import com.backbase.deferredresources.compose.test.assertGenericValueEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeAdapter::class)
internal class DeferredDrawableAdapterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test fun rememberResolvedPainter_paintsDrawableAndHasExpectedSize() {
        val deferred = DeferredDrawable.Resource(R.drawable.oval)

        composeTestRule.setContent {
            val painter = rememberResolvedPainter(deferred)
            Icon(
                painter = painter,
                contentDescription = "Test icon",
                modifier = Modifier.size(44.dp),
            )

            painter.intrinsicSize
            GenericValueNode(
                value = painter.intrinsicSize,
                modifier = TestTagModifier,
            )
        }

        composeTestRule.onNodeWithTag(TestTag).assertGenericValueEquals(Size(80f, 80f))
        // TODO: Assert something about the color, which is currently always black instead of the defined color.
    }
}
