package com.backbase.deferredresources.compose

/**
 * Indicates an API implementing support for Jetpack Compose within Deferred Resources. Such APIs are likely to break
 * as Compose changes and best practices are iterated upon.
 */
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn
public annotation class ExperimentalDeferredResourcesComposeSupport
