/*
 * Copyright 2020 Backbase R&D B.V.
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

@file:JvmName("DeferredResourcesViewUtils")

package com.backbase.deferredresources.viewx

import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDimension
import com.backbase.deferredresources.DeferredDrawable
import com.backbase.deferredresources.DeferredText

/**
 * Resolve [deferredBackground] and set the background of the view to the resolved Drawable, or remove the background if
 * the resolved drawable is null.
 *
 * If the background has padding, the view's padding is set to the resolved background's padding. However, when a
 * background is removed, this View's padding isn't touched. If setting the padding is desired, please use
 * [View.setPadding].
 */
public fun View.setBackground(deferredBackground: DeferredDrawable) {
    ViewCompat.setBackground(this, deferredBackground.resolve(context))
}

/**
 * Resolves [deferredColor] and sets the background color for this view.
 */
public fun View.setBackgroundColor(deferredColor: DeferredColor): Unit =
    setBackgroundColor(deferredColor.resolve(context))

/**
 * Resolves [deferredTintList] and applies the resolved color as a tint to the background drawable.
 *
 * This will always take effect when running on API v21 or newer. When running on platforms previous to API v21, it will
 * only take effect if the view implements the [androidx.core.view.TintableBackgroundView] interface.
 */
public fun View.setBackgroundTintList(deferredTintList: DeferredColor): Unit =
    ViewCompat.setBackgroundTintList(this, deferredTintList.resolveToStateList(context))

/**
 * Resolve [deferredForeground] and supply the resolved Drawable to be rendered on top of all of the content in the
 * view.
 */
@RequiresApi(23)
public fun View.setForeground(deferredForeground: DeferredDrawable) {
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
public fun View.setForegroundTintList(deferredTint: DeferredColor) {
    foregroundTintList = deferredTint.resolveToStateList(context)
}

/**
 * Resolves [deferredMinWidth] and sets the minimum width of the view.
 *
 * It is not guaranteed the view will be able to achieve this minimum width (for example, if its parent layout
 * constrains it with less available width).
 */
public fun View.setMinimumWidth(deferredMinWidth: DeferredDimension) {
    minimumWidth = deferredMinWidth.resolveAsSize(context)
}

/**
 * Resolves [deferredMinHeight] and sets the minimum height of the view.
 *
 * It is not guaranteed the view will be able to achieve this minimum height (for example, if its parent layout
 * constrains it with less available height).
 */
public fun View.setMinimumHeight(deferredMinHeight: DeferredDimension) {
    minimumHeight = deferredMinHeight.resolveAsSize(context)
}

/**
 * Resolves each of [deferredLeft], [deferredTop], [deferredRight], and [deferredBottom] and sets the padding.
 */
public fun View.setPadding(
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
public fun View.setPaddingRelative(
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
public fun View.setTranslationX(deferredTranslationX: DeferredDimension) {
    translationX = deferredTranslationX.resolveExact(context)
}

/**
 * Resolves [deferredTranslationY] and sets the vertical location of this view relative to its [top][View.getTop]
 * position. This effectively positions the object post-layout, in addition to wherever the object's layout placed it.
 */
public fun View.setTranslationY(deferredTranslationY: DeferredDimension) {
    translationY = deferredTranslationY.resolveExact(context)
}

/**
 * Resolves [deferredTranslationZ] and sets the depth location of this view relative to its
 * [elevation][View.getElevation].
 */
public fun View.setTranslationZ(deferredTranslationZ: DeferredDimension) {
    ViewCompat.setTranslationZ(this, deferredTranslationZ.resolveExact(context))
}

/**
 * Resolve [deferredOffset] and offset this view's horizontal location by the resolved amount.
 */
public fun View.offsetLeftAndRight(deferredOffset: DeferredDimension): Unit =
    offsetLeftAndRight(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredOffset] and offset this view's vertical location by the resolved amount.
 */
public fun View.offsetTopAndBottom(deferredOffset: DeferredDimension): Unit =
    offsetTopAndBottom(deferredOffset.resolveAsOffset(context))

/**
 * Resolve [deferredLength] and set the size of the faded edge used to indicate that more content in this view is
 * available.
 *
 * Will not change whether the fading edge is enabled; use [View.setVerticalFadingEdgeEnabled] or
 * [View.setHorizontalFadingEdgeEnabled] to enable the fading edge for the vertical or horizontal fading edges.
 */
public fun View.setFadingEdgeLength(deferredLength: DeferredDimension): Unit =
    setFadingEdgeLength(deferredLength.resolveAsSize(context))

/**
 * Resolves [deferredDistance] and sets the distance along the Z axis from the camera to this view.
 *
 * @see View.setCameraDistance
 */
public fun View.setCameraDistance(deferredDistance: DeferredDimension) {
    cameraDistance = deferredDistance.resolveExact(context)
}

/**
 * Resolves [deferredX] and sets the visual x position of this view. This is equivalent to setting the
 * [translationX][setTranslationX] property to be the difference between the x value passed in and the current
 * [left][View.getLeft] property.
 */
public fun View.setX(deferredX: DeferredDimension) {
    x = deferredX.resolveExact(context)
}

/**
 * Resolves [deferredY] and sets the visual y position of this view. This is equivalent to setting the
 * [translationY][setTranslationY] property to be the difference between the y value passed in and the current
 * [top][View.getTop] property.
 */
public fun View.setY(deferredY: DeferredDimension) {
    y = deferredY.resolveExact(context)
}

/**
 * Resolves [deferredZ] and sets the visual z position of this view. This is equivalent to setting the
 * [translationZ][setTranslationZ] property to be the difference between the z value passed in and the current
 * [elevation][View.getElevation] property.
 *
 * On API < 21, this is a no-op.
 */
public fun View.setZ(deferredZ: DeferredDimension): Unit =
    ViewCompat.setZ(this, deferredZ.resolveExact(context))

/**
 * Resolves [deferredElevation] and sets the base elevation of this view.
 */
public fun View.setElevation(deferredElevation: DeferredDimension): Unit =
    ViewCompat.setElevation(this, deferredElevation.resolveExact(context))

/**
 * Sets the View's content description to the resolved [deferredContentDescription].
 *
 * A content description briefly describes the view and is primarily used for accessibility support to determine how a
 * view should be presented to the user. In the case of a view with no textual representation, a useful content
 * description explains what the view does. For example, an image button with a phone icon that is used to place a call
 * may use "Call" as its content description. An image of a floppy disk that is used to save a file may use "Save".
 */
public fun View.setContentDescription(deferredContentDescription: DeferredText) {
    contentDescription = deferredContentDescription.resolve(context)
}

/**
 * Convenience method for sending a [android.view.accessibility.AccessibilityEvent.TYPE_ANNOUNCEMENT] to suggest that an
 * accessibility service announce the resolved [deferredText] to its users.
 *
 * Note: The event generated with this API carries no semantic meaning, and is appropriate only in exceptional
 * situations. Apps can generally achieve correct behavior for accessibility by accurately supplying the semantics of
 * their UI. They should not need to specify what exactly is announced to users.
 */
@RequiresApi(16)
public fun View.announceForAccessibility(deferredText: DeferredText): Unit =
    announceForAccessibility(deferredText.resolve(context))

/**
 * Resolve [deferredAccessibilityPaneTitle] and set it as the View's accessibility pane title. A null
 * [deferredAccessibilityPaneTitle] indicates that the View is not a pane.
 *
 * Visually distinct portion of a window with window-like semantics are considered panes for accessibility purposes. One
 * example is the content view of a fragment that is replaced. In order for accessibility services to understand a
 * pane's window-like behavior, panes should have descriptive titles. Views with pane titles produce
 * [android.view.accessibility.AccessibilityEvent]s when they appear, disappear, or change title.
 *
 * This function is a no-op on Android API < 19.
 */
public fun View.setAccessibilityPaneTitle(deferredAccessibilityPaneTitle: DeferredText?): Unit =
    ViewCompat.setAccessibilityPaneTitle(this, deferredAccessibilityPaneTitle?.resolve(context))
