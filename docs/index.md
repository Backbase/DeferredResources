# Deferred Resources

Deferred Resources is an Android library that decouples resource declaration (without Context) from
resource resolution (with Context). This allows the resolver (Activity, Fragment, or View) to be
agnostic to the source (standard resources, attributes, or values fetched from an API), and allows
repository/configuration layers to remain ignorant of Android's Context.

## Example

In the logic layer, declare the resource values however you like, without worrying about their
resolution:
```kotlin
// Resource/attribute-based text and color:
class LocalViewModel : MyViewModel {
    override fun getText(): DeferredText = DeferredText.Resource(R.string.someText)
    override fun getTextColor(): DeferredColor = DeferredColor.Attribute(R.attr.colorOnBackground)
}

// API-based text and color:
class RemoteViewModel(private val api: Api) : MyViewModel {
    override fun getText(): DeferredText = DeferredText.Constant(api.fetchText())
    override fun getTextColor(): DeferredColor = DeferredColor.Constant(api.fetchTextColor())
}
```

In the view layer, resolve a deferred resource to display it:
```kotlin
val text: DeferredText = viewModel.getText()
val textColor: DeferredColor = viewModel.getTextColor()
textView.text = text.resolve(context)
textView.setTextColor(textColor.resolve(context))
```

### Import

To use Deferred Resources, add the library as a dependency to your Android module:

```groovy
dependencies {
    implementation "com.backbase.oss.deferredresources:deferred-resources:$version"
}
```
