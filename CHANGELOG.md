# CHANGELOG

## 1.6.0
_2021-09-29_

Introduce the `deferred-resources-animation-lottie` artifact for supporting Lottie animations. 
This provides APIs for resolving existing Deferred Drawable types into a `LottieDrawable`.

Compile with Kotlin 1.5.21 and AndroidX Core-KTX 1.5.0.

## 1.5.0
_2021-07-03_

Introduce the `deferred-resources-compose-adapter` artifact for Jetpack Compose UI. This provides
APIs for resolving existing Deferred Resources types in a Compose UI context. For example,
`rememberResolvedColor` resolves a `DeferredColor` as an `androidx.compose.ui.graphics.Color`.

Compile with Kotlin 1.5.10 and AndroidX Core-KTX 1.5.0.

## 1.4.0
_2021-04-23_

Add the ability to define a `DeferredDimension.Constant` with `DP` and `SP` units, in addition to
the default `PX`.

## 1.3.0
_2021-02-19_

Add a number of `Parcelable` deferred types, such as `ParcelableDeferredText` and
`ParcelableDeferredColor`. Ensure each concrete deferred type shipped in this library, such as
`DeferredText.Constant` and `DeferredText.Resource`, implements a `Parcelable` parent interface.

`DeferredDrawable` and `DeferredTypeface` are the only types without `Parcelable` options, because
their resolved types are also not `Parcelable`.

## 1.2.1
_2021-01-16_

Compiled with Kotlin 1.4.21.

## 1.2.0
_2020-11-04_

Introduce `DeferredDrawable.Attribute` to support drawable attribute resolution.

## 1.1.0
_2020-10-28_

Introduce `SdkIntDeferredColor`, a `DeferredColor` implementation that delegates to different
`DeferredColor` sources depending on the Android OS runtime version.

Add a few extension functions for accessibility to the view extensions library, including
`setContentDescription`.

Use AppCompat to improve DeferredColor and DeferredDrawable implementations on Android OS
versions < 23. In particular, default DeferredColor implementations can resolve attribute-backed
color selectors, and default DeferredDrawable implementations can resolve vector drawables and tints
correctly.

Compile with Kotlin 1.4.10 and publish the source code in
[explicit mode](https://kotlinlang.org/docs/reference/whatsnew14.html#explicit-api-mode-for-library-authors).

Target Android SDK 30.

## 1.0.0
_2020-07-29_

🚀 Official stable release!

Add `DeferredColor.resolveToStateList` to support resolving color `<selector>` elements from XML
resources and attributes.

## 0.5.1
_2020-07-22_

Update internal `DeferredColor.Resource` and `DeferredColor.Attribute` logic to resolve the default
color of color selector resources instead of crashing.

## 0.5.0
_2020-07-14_

**Breaking change**: Update DeferredDrawable.Resource's `transformations` constructor parameter to
receive the resolving Context as a parameter. This breaks binary compatibility by removing
constructors with a Function1 parameter in their signature (replaced by a Function2 parameter). This
also breaks source compatibility for Java consumers but not for Kotlin consumers.

Add `deferred-resources-view-extensions` to simplify use of Deferred Resources with Views. Basic
extensions are provided for View, TextView, and ImageView.

Add `DeferredColorWithAlpha` along with a corresponding `withAlpha` extension on `DeferredColor`, to
make alpha transformations on unresolved `DeferredColor`s simple.

## 0.4.0
_2020-06-23_

Add `DeferredDimension.Attribute` to support dimension attribute resolution. Add
`DeferredText.resolveToString` as a convenience for converting the resolve CharSequence to a string.

Update minimum supported Java version to Java 8.

## 0.3.1
_2020-06-17_

Add the `@Px` annotation to the integer pixel value parameter of `DeferredDimension.Constant`'s
convenience constructor.

## 0.3.0
_2020-06-09_

Add a `text` package with various text helpers allowing conversion formatted and/or pluralized text
and regular deferred text when partial resolution information is known (i.e. format arguments and/or
quantity).

Add `DeferredColorDrawable` as a convenience for deferred creation of a solid-color Drawable.

Add a convenience constructor for parsing a color string to create a `DeferredColor.Constant`.

## 0.2.0
_2020-05-22_

Add `DeferredTypeface`, `DeferredIntegerArray`, and `DeferredTextArray`.

## 0.1.0
_2020-05-15_

Initial preview release. Declare booleans, colors, dimensions, drawables, integers, plurals, and
text from resources or code without a Context, relying on the consumer to resolve them.
