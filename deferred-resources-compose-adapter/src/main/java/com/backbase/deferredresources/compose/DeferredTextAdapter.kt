/*
 * Copyright 2021 Backbase R&D B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.backbase.deferredresources.compose

import android.graphics.Typeface
import android.os.Build
import android.text.SpannedString
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.em
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.text.resolveToString

/**
 * Resolve [deferredText], remembering the resulting value as long as the current [LocalContext] doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedAnnotatedString(deferredText: DeferredText): AnnotatedString {
    val context = LocalContext.current
    return remember(deferredText) {
        when (val text = deferredText.resolve(context)) {
            is AnnotatedString -> text
            is SpannedString -> AnnotatedString(text)
            else -> AnnotatedString(text.toString())
        }
    }
}

//region SpannedString conversion
@Suppress("detekt.FunctionNaming") // Factory
private fun AnnotatedString(text: SpannedString) = AnnotatedString(
    text = text.toString(),
    spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>().apply {
        addSpansFromText<StyleSpan>(text)
        addSpansFromText<UnderlineSpan>(text)
        addSpansFromText<StrikethroughSpan>(text)
        addSpansFromText<SuperscriptSpan>(text)
        addSpansFromText<SubscriptSpan>(text)
        addSpansFromText<ForegroundColorSpan>(text)
        addSpansFromText<TypefaceSpan>(text)
        addSpansFromText<RelativeSizeSpan>(text)
    },
    // TODO: Parse paragraph styles
)

private inline fun <reified T : CharacterStyle> MutableList<AnnotatedString.Range<SpanStyle>>.addSpansFromText(
    text: SpannedString
) = addAll(spansFromText<T>(text))

private inline fun <reified T : CharacterStyle> spansFromText(
    text: SpannedString
): List<AnnotatedString.Range<SpanStyle>> =
    text.getSpans(0, text.length, T::class.java)
        .mapNotNull { it.toRangeInText(text) }

@Suppress("FunctionName") // Factory
private fun CharacterStyle.toRangeInText(text: SpannedString): AnnotatedString.Range<SpanStyle>? {
    return when (val spanStyle = toSpanStyle()) {
        null -> null
        else -> AnnotatedString.Range(
            item = spanStyle,
            start = text.getSpanStart(this),
            end = text.getSpanEnd(this),
        )
    }
}

private fun CharacterStyle.toSpanStyle(): SpanStyle? = when (this) {
    is StyleSpan -> SpanStyle(
        fontWeight = if (isBold) FontWeight.Bold else null,
        fontStyle = if (isItalic) FontStyle.Italic else null,
    )
    is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
    is StrikethroughSpan -> SpanStyle(textDecoration = TextDecoration.LineThrough)
    is SuperscriptSpan -> SpanStyle(baselineShift = BaselineShift.Superscript)
    is SubscriptSpan -> SpanStyle(baselineShift = BaselineShift.Subscript)
    is ForegroundColorSpan -> SpanStyle(color = Color(foregroundColor))
    is TypefaceSpan -> SpanStyle(fontFamily = fontFamily)
    is RelativeSizeSpan -> SpanStyle(fontSize = sizeChange.em)
    else -> null
}

private val StyleSpan.isBold: Boolean
    get() = style and Typeface.BOLD != 0

private val StyleSpan.isItalic: Boolean
    get() = style and Typeface.ITALIC != 0

private val TypefaceSpan.fontFamily: FontFamily?
    get() = when (family) {
        "sans-serif" -> FontFamily.SansSerif
        "serif" -> FontFamily.Serif
        "monospace" -> FontFamily.Monospace
        "cursive" -> FontFamily.Cursive
        null -> if (Build.VERSION.SDK_INT < 28) null else typeface?.let { FontFamily(it) }
        else -> null
    }
//endregion

/**
 * Resolve [deferredText] to a plain string, remembering the resulting value as long as the current [LocalContext]
 * doesn't change.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedString(deferredText: DeferredText): String {
    val context = LocalContext.current
    return remember(deferredText) {
        deferredText.resolveToString(context)
    }
}
