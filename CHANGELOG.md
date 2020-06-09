# CHANGELOG

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
