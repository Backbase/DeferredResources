# Deferred Resources

Deferred Resources is an Android library that decouples resource declaration (without Context) from
resource resolution (with Context). This allows the resolver (Activity, Fragment, or View) to be
agnostic to the source (standard resources, attributes, or values fetched from an API), and allows
repository/configuration layers to remain ignorant of Android's Context.

## Examples

In the logic layer, declare the resource values however you like, without worrying about their
resolution or about Context:
```kotlin
// Resource/attribute-based text and color:
class LocalViewModel : MyViewModel {
    override fun getText(): DeferredText = DeferredText.Resource(R.string.someText)
    override fun getTextColor(): DeferredColor = DeferredColor.Attribute(R.attr.colorOnBackground)
    override fun getTextSize(): DeferredDimension = DeferredDimension.Attribute(R.attr.bodyTextSize)
}

// API-based text and color:
class RemoteViewModel(private val api: Api) : MyViewModel {
    override fun getText(): DeferredText = DeferredText.Constant(api.fetchText())
    override fun getTextColor(): DeferredColor = DeferredColor.Constant(api.fetchTextColor())
    override fun getTextSize(): DeferredDimension =
        DeferredDimension.Constant(api.fetchTextSize(), DeferredDimension.Constant.Unit.SP)
}
```

In the view layer, resolve a deferred resource to display it:
```kotlin
val text: DeferredText = viewModel.getText()
val textColor: DeferredColor = viewModel.getTextColor()
val textSize: DeferredDimension = viewModel.getTextSize()
textView.text = text.resolve(context)
textView.setTextColor(textColor.resolve(context))
textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, deferredSize.resolveExact(context))
```

With view extensions, this is even simpler. The View's own Context is used to resolve any deferred
resource types:
```kotlin
textView.setText(viewModel.getText())
textView.setTextColor(viewModel.getTextColor())
textView.setTextSize(viewModel.getTextSize())
```

### Text types

Various types of text are supported: `DeferredText` for basic text, `DeferredFormattedString` for
formatted text, `DeferredPlurals` for pluralized text, and `DeferredFormattedPlurals` for formatted,
pluralized text. Additionally, it's possible to "partially resolve" these more complex text types to
be more basic without yet having a `Context`.

```kotlin
val deferredFormattedPlurals = DeferredFormattedPlurals.Resource(R.plurals.formatted_plurals)

// If you have the format args, quantity, and Context:
val string: String = deferredFormattedPlurals.resolve(context, 5, "million")

// If you have the format args and quantity, but no Context:
val deferredText: DeferredText = deferredFormattedPlurals.withQuantityAndFormatArgs(5, "million")

// If you have only the quantity:
val deferredFormattedString: DeferredFormattedString = deferredFormattedPlurals.withQuantity(5)

// If you have only the format args:
val deferredPlurals: DeferredPlurals = deferredFormattedPlurals.withFormatArgs("million")
```

All text-related types can eventually be converted to `DeferredText` through similar extensions.

### Jetpack Compose UI

For each Deferred Resources type, the experimental `deferred-resource-compose-adapter` library
offers a `remember*` function to resolve the Deferred item to a standard Compose UI type.

```kotlin
val color: Color = rememberResolvedColor(deferredColor)
val size: Dp = rememberResolvedDp(deferredDimension)
val icon: Painter = rememberResolvedPainter(deferredDrawable)
```

All of these APIs are marked as `@ExperimentalComposeAdapter` and should not be considered stable.
Their behavior and binary compatibility are not guaranteed.

## Import

To use Deferred Resources, add the library as a dependency to your Android module:

```groovy
dependencies {
    implementation "com.backbase.oss.deferredresources:deferred-resources:$version"
    implementation "com.backbase.oss.deferredresources:deferred-resources-view-extensions:$version"
    implementation "com.backbase.oss.deferredresources:deferred-resources-compose-adapter:$version"
}
```
