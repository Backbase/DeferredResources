@file:JvmName("ViewDeferredUtils")

package com.backbase.deferredresources.extensions

import android.view.View
import androidx.annotation.RequiresApi
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

/**
 * Resolves [deferredTranslationX] and sets the horizontal location of this view relative to its [left][View.getLeft]
 * position. This effectively positions the object post-layout, in addition to wherever the object's layout placed it.
 */
fun View.setTranslationX(deferredTranslationX: DeferredDimension) {
    translationX = deferredTranslationX.resolveExact(context)
}

/**
 * Resolves [deferredTranslationY] and sets the vertical location of this view relative to its [top][View.getTop]
 * position. This effectively positions the object post-layout, in addition to wherever the object's layout placed it.
 */
fun View.setTranslationY(deferredTranslationY: DeferredDimension) {
    translationY = deferredTranslationY.resolveExact(context)
}

/**
 * Resolves [deferredTranslationZ] and sets the depth location of this view relative to its
 * [elevation][View.getElevation].
 */
fun View.setTranslationZ(deferredTranslationZ: DeferredDimension) {
    ViewCompat.setTranslationZ(this, deferredTranslationZ.resolveExact(context))
}

/**
 * Resolve [deferredOffset] and offset this view's horizontal location by the resolved amount.
 */
fun View.offsetLeftAndRight(deferredOffset: DeferredDimension) =
    offsetLeftAndRight(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredOffset] and offset this view's vertical location by the resolved amount.
 */
fun View.offsetTopAndBottom(deferredOffset: DeferredDimension) =
    offsetTopAndBottom(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredLength] and set the size of the faded edge used to indicate that more content in this view is
 * available.
 *
 * Will not change whether the fading edge is enabled; use [View.setVerticalFadingEdgeEnabled] or
 * [View.setHorizontalFadingEdgeEnabled] to enable the fading edge for the vertical or horizontal fading edges.
 */
fun View.setFadingEdgeLength(deferredLength: DeferredDimension) =
    setFadingEdgeLength(deferredLength.resolveAsSize(context))

/**
 * Shows the context menu for this view anchored to the resolved coordinates of [deferredX] and [deferredY].
 */
@RequiresApi(24)
fun View.showContextMenu(deferredX: DeferredDimension, deferredY: DeferredDimension) =
    showContextMenu(deferredX.resolveExact(context), deferredY.resolveExact(context))

/**
 * Resolve [deferredValue] and set the horizontal scrolled position of your view. This will cause a call to
 * [View.onScrollChanged] and the view will be invalidated.
 */
fun View.setScrollX(deferredValue: DeferredDimension) {
    scrollX = deferredValue.resolveAsOffset(context)
}

/**
 * Resolve [deferredValue] and set the vertical scrolled position of your view. This will cause a call to
 * [View.onScrollChanged] and the view will be invalidated.
 */
fun View.setScrollY(deferredValue: DeferredDimension) {
    scrollY = deferredValue.resolveAsOffset(context)
}

/**
 * Resolve [deferredX] and [deferredY] and move the scrolled position of your view. This will cause a call to
 * [View.onScrollChanged] and the view will be invalidated.
 */
fun View.scrollBy(
    deferredX: DeferredDimension = DeferredDimension.Constant(0),
    deferredY: DeferredDimension = DeferredDimension.Constant(0)
) = scrollBy(deferredX.resolveAsOffset(context), deferredY.resolveAsOffset(context))

/**
 * Resolves [deferredDistance] and sets the distance along the Z axis from the camera to this view.
 *
 * @see View.setCameraDistance
 */
fun View.setCameraDistance(deferredDistance: DeferredDimension) {
    cameraDistance = deferredDistance.resolveExact(context)
}

/**
 * Resolves [deferredX] and sets the visual x position of this view. This is equivalent to setting the
 * [translationX][setTranslationX] property to be the difference between the x value passed in and the current
 * [left][View.getLeft] property.
 */
fun View.setX(deferredX: DeferredDimension) {
    x = deferredX.resolveExact(context)
}

/**
 * Resolves [deferredY] and sets the visual y position of this view. This is equivalent to setting the
 * [translationY][setTranslationY] property to be the difference between the y value passed in and the current
 * [top][View.getTop] property.
 */
fun View.setY(deferredY: DeferredDimension) {
    y = deferredY.resolveExact(context)
}

/**
 * Resolves [deferredZ] and sets the visual z position of this view. This is equivalent to setting the
 * [translationZ][setTranslationZ] property to be the difference between the z value passed in and the current
 * [elevation][View.getElevation] property.
 *
 * On API < 21, this is a no-op.
 */
fun View.setZ(deferredZ: DeferredDimension) =
    ViewCompat.setZ(this, deferredZ.resolveExact(context))

/**
 * Resolves [deferredElevation] and sets the base elevation of this view.
 */
fun View.setElevation(deferredElevation: DeferredDimension) =
    ViewCompat.setElevation(this, deferredElevation.resolveExact(context))
