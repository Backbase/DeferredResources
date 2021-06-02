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
import android.text.SpannedString
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.text.resolveToString

/**
 * Resolve [deferredText], remembering the resulting value as long as the current [LocalContext] doesn't change.
 *
 * Note: Currently, style elements from resource-resolved text are not kept.
 */
@ExperimentalComposeAdapter
@Composable public fun rememberResolvedAnnotatedString(deferredText: DeferredText): AnnotatedString {
    val context = LocalContext.current
    return remember(deferredText) {
        when (val text = deferredText.resolve(context)) {
            is AnnotatedString -> text
            is SpannedString -> {
                val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
                spanStyles.addSpansFromText<UnderlineSpan>(text)
                spanStyles.addSpansFromText<StyleSpan>(text)
                AnnotatedString(text.toString(), spanStyles = spanStyles)
            }
            else -> AnnotatedString(text.toString())
        }
    }
}

private inline fun <reified T : CharacterStyle> MutableList<AnnotatedString.Range<SpanStyle>>.addSpansFromText(
    text: SpannedString
) = addAll(
    with(text) {
        getSpans(0, length, T::class.java).mapNotNull {
            AnnotatedStringRange(it)
        }
    }
)

@Suppress("FunctionName") // Factory
private fun SpannedString.AnnotatedStringRange(characterStyle: CharacterStyle): AnnotatedString.Range<SpanStyle>? {
    val spanStyle = when (characterStyle) {
        is StyleSpan -> SpanStyle(
            fontWeight = if (characterStyle.style and Typeface.BOLD == 0) null else FontWeight.Bold,
            fontStyle = if (characterStyle.style and Typeface.ITALIC == 0) null else FontStyle.Italic,
        )
        is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
        else -> null
    }
    return when (spanStyle) {
        null -> null // TODO MISSING: Log a warning?
        else -> AnnotatedString.Range(
            item = spanStyle,
            start = getSpanStart(characterStyle),
            end = getSpanEnd(characterStyle),
        )
    }
}

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
