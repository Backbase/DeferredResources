package com.backbase.deferredresources.extensions

import android.widget.TextView
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.DeferredTypeface

/**
 * Resolves [deferredText] and sets it as the text to be displayed.
 */
fun TextView.setText(deferredText: DeferredText) {
    text = deferredText.resolve(context)
}

/**
 * Resolves [deferredText] and sets it as the text to be displayed with the given [type].
 */
fun TextView.setText(deferredText: DeferredText, type: TextView.BufferType) =
    setText(deferredText.resolve(context), type)

/**
 * Resolves [deferredColor] and sets the text color for all the states to be the resolved color.
 */
fun TextView.setTextColor(deferredColor: DeferredColor) = setTextColor(deferredColor.resolve(context))

/**
 * Resolves [deferredColor] and sets the color of the hint text for all the states of this TextView.
 */
fun TextView.setHintTextColor(deferredColor: DeferredColor) = setHintTextColor(deferredColor.resolve(context))

/**
 * Resolves the [deferredTypeface] and sets the typeface and style in which the text should be displayed.
 *
 * Note that not all Typeface families actually have bold and italic variants, so you may need to use [setTypeface] with
 * an explicit style to get the desired appearance.
 */
fun TextView.setTypeface(deferredTypeface: DeferredTypeface) {
    typeface = deferredTypeface.resolve(context)
}

/**
 * Resolves the [deferredTypeface] and sets the typeface along with the [style] in which the text should be displayed.
 *
 * Turns on the fake bold and italic bits in the Paint if the resolved Typeface does not have all the bits in the
 * specified [style].
 */
fun TextView.setTypeface(deferredTypeface: DeferredTypeface, style: Int) =
    setTypeface(deferredTypeface.resolve(context), style)
