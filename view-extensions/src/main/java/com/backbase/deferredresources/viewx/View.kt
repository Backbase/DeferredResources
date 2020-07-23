@file:JvmName("DeferredResourcesViewUtils")

package com.backbase.deferredresources.viewx

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
fun View.setBackgroundColor(deferredColor: DeferredColor): Unit = setBackgroundColor(deferredColor.resolve(context))

/**
 * Resolves [deferredTintList] and applies the resolved color as a tint to the background drawable.
 *
 * This will always take effect when running on API v21 or newer. When running on platforms previous to API v21, it will
 * only take effect if the view implements the [androidx.core.view.TintableBackgroundView] interface.
 */
fun View.setBackgroundTintList(deferredTintList: DeferredColor): Unit =
    ViewCompat.setBackgroundTintList(this, deferredTintList.resolveToStateList(context))

/**
 * Resolve [deferredForeground] and supply the resolved Drawable to be rendered on top of all of the content in the
 * view.
 */
@RequiresApi(23)
fun View.setForeground(deferredForeground: DeferredDrawable) {
    foreground = deferredForeground.resolve(context)
}

/**
 * Resolves [deferredTint] and applies the resolved color as a tint to the foreground drawable. Does not modify the
 * current tint mode, which is [android.graphics.PorterDuff.Mode.SRC_IN] by default.
 *
 * Subsequent calls to [setForeground] will automatically mutate the resolved drawable and apply the specified tint and
 * tint mode using [android.graphics.drawable.Drawable.setTintList].
 */
@RequiresApi(23)
fun View.setForegroundTintList(deferredTint: DeferredColor) {
    foregroundTintList = deferredTint.resolveToStateList(context)
}

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
): Unit = setPadding(
    deferredLeft.resolveAsSize(context),
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
): Unit = ViewCompat.setPaddingRelative(
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
fun View.offsetLeftAndRight(deferredOffset: DeferredDimension): Unit =
    offsetLeftAndRight(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredOffset] and offset this view's vertical location by the resolved amount.
 */
fun View.offsetTopAndBottom(deferredOffset: DeferredDimension): Unit =
    offsetTopAndBottom(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredLength] and set the size of the faded edge used to indicate that more content in this view is
 * available.
 *
 * Will not change whether the fading edge is enabled; use [View.setVerticalFadingEdgeEnabled] or
 * [View.setHorizontalFadingEdgeEnabled] to enable the fading edge for the vertical or horizontal fading edges.
 */
fun View.setFadingEdgeLength(deferredLength: DeferredDimension): Unit =
    setFadingEdgeLength(deferredLength.resolveAsSize(context))

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
fun View.setZ(deferredZ: DeferredDimension): Unit =
    ViewCompat.setZ(this, deferredZ.resolveExact(context))

/**
 * Resolves [deferredElevation] and sets the base elevation of this view.
 */
fun View.setElevation(deferredElevation: DeferredDimension): Unit =
    ViewCompat.setElevation(this, deferredElevation.resolveExact(context))
