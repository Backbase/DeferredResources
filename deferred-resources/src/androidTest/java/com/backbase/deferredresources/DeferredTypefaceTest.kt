package com.backbase.deferredresources

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontsContractCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DeferredTypefaceTest {

    private lateinit var otherThreadExecutor: ExecutorService
    private lateinit var otherThreadScope: CoroutineScope

    @Before fun setUp() {
        otherThreadExecutor = Executors.newSingleThreadExecutor()
        otherThreadScope = CoroutineScope(otherThreadExecutor.asCoroutineDispatcher())
    }

    @After fun tearDown() {
        otherThreadScope.coroutineContext.cancel()
        otherThreadExecutor.shutdownNow()
    }

    //region Constant
    @Test fun constant_resolveSync_returnsConstantValue() {
        val deferred = DeferredTypeface.Constant(Typeface.MONOSPACE)
        assertThat(deferred.resolve(context)).isEqualTo(Typeface.MONOSPACE)
    }

    @Test fun constant_resolveAsync_nullHandler_returnsConstantValueOnMainThread() {
        val deferred = DeferredTypeface.Constant(Typeface.MONOSPACE)

        val callback = TestFontCallback()
        deferred.resolve(context, callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)
        assertThat(callback.results).containsExactly(TestFontCallback.Result.Success(Typeface.MONOSPACE, mainThreadId))
    }

    @Test fun constant_resolveAsync_otherThreadHandler_returnsConstantValueOnOtherThread() {
        val deferred = DeferredTypeface.Constant(Typeface.MONOSPACE)
        val callback = TestFontCallback()

        // Get other thread's ID:
        val otherThreadId = runBlocking(otherThreadScope.coroutineContext) { currentThreadId }

        // Prepare a Looper and instantiate a Handler on the other thread:
        val otherThreadHandler: Handler = runBlocking(otherThreadScope.coroutineContext) {
            Looper.prepare()
            Handler()
        }

        // Start looping in the other thread:
        otherThreadScope.launch { Looper.loop() }

        // Resolve the typeface from this thread:
        deferred.resolve(context, callback, otherThreadHandler)

        // TODO: Wait for idle or something
        runBlocking { delay(100) }

        // Now the result has been posted on the other thread:
        assertThat(callback.results).hasSize(1)
        assertThat(callback.results).containsExactly(TestFontCallback.Result.Success(Typeface.MONOSPACE, otherThreadId))
    }
    //endregion

    //region Resource
    @Test fun resource_resolveSync_validId_resolvesWithContext() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isNotNull()
        resolved as Typeface
        assertThat(resolved.weight).isEqualTo(300)
        assertThat(resolved.style).isEqualTo(Typeface.ITALIC)
    }

    @Test(expected = Resources.NotFoundException::class)
    fun resource_resolveSync_invalidId_throwsException() {
        val deferred = DeferredTypeface.Resource(0)
        deferred.resolve(context)
    }

    @Test fun resource_resolveSync_restrictedContext_returnsNull() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        assertThat(deferred.resolve(context.createRestrictedContext())).isNull()
    }

    @Test fun resource_resolveAsync_nullHandler_resolvesWithContextOnMainThread() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        val callback = TestFontCallback()

        deferred.resolve(context, callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result.threadId).isEqualTo(mainThreadId)
        assertThat(result).isInstanceOf(TestFontCallback.Result.Success::class.java)

        val resolved = (result as TestFontCallback.Result.Success).typeface
        assertThat(resolved.weight).isEqualTo(300)
        assertThat(resolved.style).isEqualTo(Typeface.ITALIC)
    }

    @Test fun resource_resolveAsync_otherThreadHandler_resolvesWithContextOnOtherThread() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        val callback = TestFontCallback()

        // Get other thread's ID:
        val otherThreadId = runBlocking(otherThreadScope.coroutineContext) { currentThreadId }

        // Prepare a Looper and instantiate a Handler on the other thread:
        val otherThreadHandler: Handler = runBlocking(otherThreadScope.coroutineContext) {
            Looper.prepare()
            Handler()
        }

        // Start looping in the other thread:
        otherThreadScope.launch { Looper.loop() }

        // Resolve the typeface from this thread:
        deferred.resolve(context, callback, otherThreadHandler)

        // TODO: Wait for idle or something
        runBlocking { delay(100) }

        // Now the result has been posted on the other thread:
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result.threadId).isEqualTo(otherThreadId)
        assertThat(result).isInstanceOf(TestFontCallback.Result.Success::class.java)

        val resolved = (result as TestFontCallback.Result.Success).typeface
        assertThat(resolved.weight).isEqualTo(300)
        assertThat(resolved.style).isEqualTo(Typeface.ITALIC)
    }

    @Test(expected = Resources.NotFoundException::class)
    fun resource_resolveAsync_invalidId_throwsException() {
        val deferred = DeferredTypeface.Resource(0)
        deferred.resolve(context, TestFontCallback())
    }

    @Test fun resource_resolveAsync_restrictedContext_postsError() {
        val deferred = DeferredTypeface.Resource(R.font.merriweather_light_italic)
        val callback = TestFontCallback()

        deferred.resolve(context.createRestrictedContext(), callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)
        val result = callback.results[0]
        assertThat(result).isInstanceOf(TestFontCallback.Result.Failure::class.java)
        result as TestFontCallback.Result.Failure
        assertThat(result.reason).isEqualTo(FontsContractCompat.FontRequestCallback.FAIL_REASON_SECURITY_VIOLATION)
    }
    //endregion

    //region Asset
    @Test fun asset_resolveSync_validFontFile_resolvesWithContext() {
        val deferredDark = DeferredTypeface.Asset("merriweather_bold.ttf")
        val resolved = deferredDark.resolve(context)

        assertThat(resolved.weight).isEqualTo(700)
        assertThat(resolved.style).isEqualTo(Typeface.BOLD)
    }

    @Test(expected = RuntimeException::class)
    fun asset_resolveSync_nonexistentFontFile_throwsException() {
        val deferred = DeferredTypeface.Asset("nothing.ttf")
        deferred.resolve(context)
    }

    @Test fun asset_resolveSync_nonFontFile_returnsDefaultTypeface() {
        val deferred = DeferredTypeface.Asset("invalid.txt")
        val resolved = deferred.resolve(context)
        assertThat(resolved).isEqualTo(Typeface.DEFAULT)
    }

    @Test fun asset_resolveAsync_nullHandler_resolvesWithContextOnMainThread() {
        val deferred = DeferredTypeface.Asset("merriweather_bold.ttf")

        val callback = TestFontCallback()
        deferred.resolve(context, callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result.threadId).isEqualTo(mainThreadId)
        assertThat(result).isInstanceOf(TestFontCallback.Result.Success::class.java)

        val resolved = (result as TestFontCallback.Result.Success).typeface
        assertThat(resolved.weight).isEqualTo(700)
        assertThat(resolved.style).isEqualTo(Typeface.BOLD)
    }

    @Test fun asset_resolveAsync_otherThreadHandler_resolvesWithContextOnOtherThread() {
        val deferred = DeferredTypeface.Asset("merriweather_bold.ttf")
        val callback = TestFontCallback()

        // Get other thread's ID:
        val otherThreadId = runBlocking(otherThreadScope.coroutineContext) { currentThreadId }

        // Prepare a Looper and instantiate a Handler on the other thread:
        val otherThreadHandler: Handler = runBlocking(otherThreadScope.coroutineContext) {
            Looper.prepare()
            Handler()
        }

        // Start looping in the other thread:
        otherThreadScope.launch { Looper.loop() }

        // Resolve the typeface from this thread:
        deferred.resolve(context, callback, otherThreadHandler)

        // TODO: Wait for idle or something
        runBlocking { delay(100) }

        // Now the result has been posted on the other thread:
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result.threadId).isEqualTo(otherThreadId)
        assertThat(result).isInstanceOf(TestFontCallback.Result.Success::class.java)

        val resolved = (result as TestFontCallback.Result.Success).typeface
        assertThat(resolved.weight).isEqualTo(700)
        assertThat(resolved.style).isEqualTo(Typeface.BOLD)
    }

    @Test fun asset_resolveAsync_nonexistentFontFile_postsErrorNotFound() {
        val deferred = DeferredTypeface.Asset("nothing.ttf")
        val callback = TestFontCallback()

        deferred.resolve(context, callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result).isInstanceOf(TestFontCallback.Result.Failure::class.java)

        result as TestFontCallback.Result.Failure
        assertThat(result.reason).isEqualTo(FontsContractCompat.FontRequestCallback.FAIL_REASON_FONT_NOT_FOUND)
    }

    @Test fun asset_resolveAsync_nonFontFile_returnsDefaultTypeface() {
        val deferred = DeferredTypeface.Asset("invalid.txt")
        val callback = TestFontCallback()

        deferred.resolve(context, callback)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertThat(callback.results).hasSize(1)

        val result = callback.results[0]
        assertThat(result).isInstanceOf(TestFontCallback.Result.Success::class.java)

        result as TestFontCallback.Result.Success
        assertThat(result.typeface).isEqualTo(Typeface.DEFAULT)
    }
    //endregion

    private class TestFontCallback : ResourcesCompat.FontCallback() {

        val results: List<Result> get() = _results
        private val _results = mutableListOf<Result>()

        override fun onFontRetrieved(typeface: Typeface) {
            synchronized(_results) {
                _results.add(Result.Success(typeface, currentThreadId))
            }
        }

        override fun onFontRetrievalFailed(reason: Int) {
            synchronized(_results) {
                _results.add(Result.Failure(reason, currentThreadId))
            }
        }

        sealed class Result {
            abstract val threadId: Long

            data class Success(
                val typeface: Typeface,
                override val threadId: Long
            ) : Result()

            data class Failure(
                val reason: Int,
                override val threadId: Long
            ) : Result()
        }
    }

    private companion object {
        private val currentThreadId: Long
            get() = Thread.currentThread().id

        private val mainThreadId: Long
            get() = Looper.getMainLooper().thread.id
    }
}
