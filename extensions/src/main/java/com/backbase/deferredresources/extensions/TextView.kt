package com.backbase.deferredresources.extensions

import android.util.TypedValue
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
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

/**
 * Resolve [deferredSize] and set the default text size to the resolved value.
 *
 * Note: if this TextView has the auto-size feature enabled than this function is no-op.
 */
fun TextView.setTextSize(deferredSize: DeferredDimension) =
    setTextSize(TypedValue.COMPLEX_UNIT_PX, deferredSize.resolveExact(context))

/**
 * Specify whether this widget should automatically scale the text to try to perfectly fit within the layout bounds. If
 * the resolved [deferredMinTextSize], [deferredMaxTextSize], and [deferredStepGranularity] are valid, the type of
 * auto-size is set to [TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM].
 *
 * @throws IllegalArgumentException if any of the configuration params are invalid.
 */
fun TextView.setAutoSizeTextTypeUniformWithConfiguration(
    deferredMinTextSize: DeferredDimension, deferredMaxTextSize: DeferredDimension,
    deferredStepGranularity: DeferredDimension
) = TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
    this,
    deferredMinTextSize.resolveAsSize(context), deferredMaxTextSize.resolveAsSize(context),
    deferredStepGranularity.resolveAsSize(context),
    TypedValue.COMPLEX_UNIT_PX
)

/**
 * Resolves [deferredLineHeight] and sets an explicit line height for this TextView. This is equivalent to the vertical
 * distance between subsequent baselines in the TextView.
 */
fun TextView.setLineHeight(deferredLineHeight: DeferredDimension) =
    TextViewCompat.setLineHeight(this, deferredLineHeight.resolveAsSize(context))

/**
 * Resolves [deferredFirstBaselineToTopHeight] and updates the top padding of the TextView so that the resolved height
 * is equal to the distance between the first text baseline and the top of this TextView.
 *
 * Note: If `FontMetrics.top` or `FontMetrics.ascent` was already greater than the resolved height, the top padding is
 * not updated.
 */
fun TextView.setFirstBaselineToTopHeight(deferredFirstBaselineToTopHeight: DeferredDimension) =
    TextViewCompat.setFirstBaselineToTopHeight(this, deferredFirstBaselineToTopHeight.resolveAsSize(context))

/**
 * Resolves [deferredLastBaselineToBottomHeight] and updates the bottom padding of the TextView so that the resolved
 * height is equal to the distance between the last text baseline and the bottom of this TextView.
 *
 * Note: If `FontMetrics.bottom` or `FontMetrics.descent` was already greater than the resolved height, the bottom
 * padding is not updated.
*/
fun TextView.setLastBaselineToBottomHeight(deferredLastBaselineToBottomHeight: DeferredDimension) =
    TextViewCompat.setLastBaselineToBottomHeight(this, deferredLastBaselineToBottomHeight.resolveAsSize(context))

/**
 * Resolves [deferredMinHeight] and sets the height of the TextView to be at least that tall.
 *
 * This value is used for height calculation if LayoutParams does not force TextView to have an exact height. Setting
 * this value overrides previous minimum height configurations such as [TextView.setMinLines] or [TextView.setLines].
 *
 * The value given here is different than [setMinimumHeight]. Between this and the value set in [setMinimumHeight], the
 * greater one is used to decide the final height.
 */
fun TextView.setMinHeight(deferredMinHeight: DeferredDimension) {
    minHeight = deferredMinHeight.resolveAsSize(context)
}

/**
 * Resolves [deferredMaxHeight] and sets the height of the TextView to be at most that tall.
 *
 * This value is used for height calculation if LayoutParams does not force TextView to have an exact height. Setting
 * this value overrides previous maximum height configurations such as [TextView.setMaxLines] or [TextView.setLines].
 */
fun TextView.setMaxHeight(deferredMaxHeight: DeferredDimension) {
    maxHeight = deferredMaxHeight.resolveAsSize(context)
}

/**
 * Resolves [deferredHeight] and sets the height of the TextView to be exactly that tall.
 *
 * This value is used for height calculation if LayoutParams does not force TextView to have an exact height. Setting
 * this value overrides previous minimum/maximum height configurations such as [setMinHeight] or [setMaxHeight].
 */
fun TextView.setHeight(deferredHeight: DeferredDimension) {
    height = deferredHeight.resolveAsSize(context)
}

/**
 * Resolves [deferredMinWidth] and sets the width of the TextView to be at least that wide.
 *
 * This value is used for width calculation if LayoutParams does not force TextView to have an exact width. Setting
 * this value overrides previous minimum width configurations such as [TextView.setMinEms] or [TextView.setEms].
 *
 * The value given here is different than [setMinimumWidth]. Between this and the value set in [setMinimumWidth], the
 * greater one is used to decide the final width.
 */
fun TextView.setMinWidth(deferredMinWidth: DeferredDimension) {
    minWidth = deferredMinWidth.resolveAsSize(context)
}

/**
 * Resolves [deferredMaxWidth] and sets the width of the TextView to be at most that wide.
 *
 * This value is used for width calculation if LayoutParams does not force TextView to have an exact width. Setting
 * this value overrides previous maximum width configurations such as [TextView.setMaxEms] or [TextView.setEms].
 */
fun TextView.setMaxWidth(deferredMaxWidth: DeferredDimension) {
    maxWidth = deferredMaxWidth.resolveAsSize(context)
}

/**
 * Resolves [deferredWidth] and sets the width of the TextView to be exactly that wide.
 *
 * This value is used for width calculation if LayoutParams does not force TextView to have an exact width. Setting
 * this value overrides previous minimum/maximum width configurations such as [setMinWidth] or [setMaxWidth].
 */
fun TextView.setWidth(deferredWidth: DeferredDimension) {
    width = deferredWidth.resolveAsSize(context)
}

/**
 * Resolves [add] and sets line spacing for this TextView. Each line other than the last line will have its height
 * multiplied by [mult] and have [add] added to it.
 */
// TODO UNDECIDED: Add and use a DeferredFloat?
fun TextView.setLineSpacing(add: DeferredDimension, mult: Float) = setLineSpacing(add.resolveExact(context), mult)
