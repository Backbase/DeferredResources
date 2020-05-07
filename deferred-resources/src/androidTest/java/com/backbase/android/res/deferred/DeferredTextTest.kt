package com.backbase.android.res.deferred

import android.graphics.Typeface
import android.text.SpannedString
import android.text.style.StyleSpan
import com.backbase.android.res.deferred.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredTextTest {

    private val richTextWithoutTags = "Rich text"

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredText.Constant("Some text")
        assertThat(deferred.resolve(context)).isEqualTo("Some text")
    }

    @Test fun resource_withTypeString_resolvesStringWithContext() {
        val deferred = DeferredText.Resource(R.string.richText)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isEqualTo(richTextWithoutTags)
        assertThat(resolved).isInstanceOf(String::class.java)
    }

    @Test fun resource_withTypeText_resolvesTextWithContext() {
        val deferred = DeferredText.Resource(R.string.richText, type = DeferredText.Resource.Type.TEXT)

        val resolved = deferred.resolve(context)

        assertThat(resolved.toString()).isEqualTo(richTextWithoutTags)

        // Verify the resolved value is not a plain string:
        assertThat(resolved).isInstanceOf(SpannedString::class.java)
        resolved as SpannedString

        // Verify the resolved value has a single BOLD style span:
        val spans = resolved.getSpans(0, resolved.length, Object::class.java)
        assertThat(spans.size).isEqualTo(1)
        val span = spans[0]
        assertThat(span).isInstanceOf(StyleSpan::class.java)
        span as StyleSpan
        assertThat(span.style).isEqualTo(Typeface.BOLD)
    }
}
