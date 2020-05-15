# Deferred Resources
[![](https://github.com/Backbase/DeferredResources/workflows/CI/badge.svg?branch=master)](https://github.com/Backbase/DeferredResources/actions?query=workflow%3ACI+branch%3Amaster)

An Android library that decouples resource declaration (without Context) from resource resolution
(with Context). This allows the resolver (Activity, Fragment, or View) to be agnostic to the source
(standard resources, attributes, or values fetched from an API), and allows repository/configuration
layers to remain ignorant of Android's Context.

## Download
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.backbase.oss.deferredresources/deferred-resources/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.backbase.oss.deferredresources/deferred-resources)

Deferred Resources is available on Maven Central. Snapshots of the development version are available
in [Sonatype's Snapshots
repository](https://oss.sonatype.org/#view-repositories;snapshots~browsestorage).

```groovy
implementation "com.backbase.oss.deferredresources:deferred-resources:$version"
```

## Use

In the view layer, resolve a deferred resource to display it:
```kotlin
val text: DeferredText = respository.getText()
val textColor: DeferredColor = repository.getTextColor()
textView.text = text.resolve(context)
textView.setTextColor(textColor.resolve(context))
```

In the logic layer, declare the resource values however you like, without worrying about their
resolution:
```kotlin
class LocalRepository : Repository {
    override fun getText(): DeferredText = DeferredText.Resource(R.string.someText)
    override fun getTextColor(): DeferredColor = DeferredColor.Attribute(R.attr.colorOnBackground)
}

class RemoteRepository(private val api: Api) : Repository {
    override fun getText(): DeferredText = DeferredText.Constant(api.fetchText())
    override fun getTextColor(): DeferredColor = DeferredColor.Constant(api.fetchTextColor())
}
```

## License
```
Copyright 2020 Backbase R&D, B.V.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
