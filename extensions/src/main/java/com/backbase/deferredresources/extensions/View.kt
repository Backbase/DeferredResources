package com.backbase.deferredresources.extensions

import android.view.View
import androidx.core.view.ViewCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.DeferredDrawable

/**
 * Resolve [deferredBackground] and set the background of the view to the resolved Drawable, or remove the background if
 * the resolved drawable is null.
 *
 * If the background has padding, the view's padding is set to the resolved background's padding. However, when a
 * background is removed, this View's padding isn't touched. If setting the padding is desired, please use
 * [View.setPadding].
 */
fun View.setBackground(deferredBackground: DeferredDrawable) {
    ViewCompat.setBackground(this, deferredBackground.resolve(context))
}

/**
 * Resolves [deferredColor] and sets the background color for this view.
 */
fun View.setBackgroundColor(deferredColor: DeferredColor) = setBackgroundColor(deferredColor.resolve(context))

/**
 * Resolves [deferredMinWidth] and sets the minimum width of the view.
 *
 * It is not guaranteed the view will be able to achieve this minimum width (for example, if its parent layout
 * constrains it with less available width).
 */
fun View.setMinimumWidth(deferredMinWidth: DeferredDimension) {
    minimumWidth = deferredMinWidth.resolveAsSize(context)
}

/**
 * Resolves [deferredMinHeight] and sets the minimum height of the view.
 *
 * It is not guaranteed the view will be able to achieve this minimum height (for example, if its parent layout
 * constrains it with less available height).
 */
fun View.setMinimumHeight(deferredMinHeight: DeferredDimension) {
    minimumHeight = deferredMinHeight.resolveAsSize(context)
}

/**
 * Resolves each of [deferredLeft], [deferredTop], [deferredRight], and [deferredBottom] and sets the padding.
 */
fun View.setPadding(
    deferredLeft: DeferredDimension = DeferredDimension.Constant(paddingLeft),
    deferredTop: DeferredDimension = DeferredDimension.Constant(paddingTop),
    deferredRight: DeferredDimension = DeferredDimension.Constant(paddingRight),
    deferredBottom: DeferredDimension = DeferredDimension.Constant(paddingBottom)
) = setPadding(
    deferredLeft.resolveAsOffset(context),
    deferredTop.resolveAsSize(context),
    deferredRight.resolveAsSize(context),
    deferredBottom.resolveAsSize(context)
)

/**
 * Resolves each of [deferredStart], [deferredTop], [deferredEnd], and [deferredBottom] and sets the relative padding.
 */
fun View.setPaddingRelative(
    deferredStart: DeferredDimension = DeferredDimension.Constant(ViewCompat.getPaddingStart(this)),
    deferredTop: DeferredDimension = DeferredDimension.Constant(paddingTop),
    deferredEnd: DeferredDimension = DeferredDimension.Constant(ViewCompat.getPaddingEnd(this)),
    deferredBottom: DeferredDimension = DeferredDimension.Constant(paddingBottom)
) = ViewCompat.setPaddingRelative(
    this,
    deferredStart.resolveAsSize(context),
    deferredTop.resolveAsSize(context),
    deferredEnd.resolveAsSize(context),
    deferredBottom.resolveAsSize(context)
)

// TODO: Translation, offsets
