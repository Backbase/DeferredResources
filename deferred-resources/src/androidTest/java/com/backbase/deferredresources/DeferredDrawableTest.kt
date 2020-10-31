package com.backbase.deferredresources

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assume.assumeFalse
import org.junit.Test

internal class DeferredDrawableTest {

    private val defaultOvalGradientFraction = 0.8f

    /**
     * Some tests change drawables without mutating them, so the change persists. Reset after each test to avoid
     * messing with other tests.
     */
    @After fun restoreDrawables() {
        // Radius XML is not honored on API 21, so mutate=false tests are skipped
        if (Build.VERSION.SDK_INT == 21) return

        val loadedAgain = AppCompatResources.getDrawable(context, R.drawable.oval) as GradientDrawable
        loadedAgain.gradientRadius = defaultOvalGradientFraction
    }

    @Test fun constant_returnsConstantValue() {
        val drawable = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(Color.RED, Color.BLACK))
        val deferred = DeferredDrawable.Constant(drawable)
        assertThat(deferred.resolve(context)).isEqualTo(drawable)
    }

    @Test fun resource_withMutateFalse_resolvesWithContext() {
        assumeFalse("XML drawable does not have correct radius on API 21", Build.VERSION.SDK_INT == 21)

        val deferred = DeferredDrawable.Resource(R.drawable.oval, mutate = false)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)

        resolved.gradientRadius = 0.5f

        // Since it's not mutated, transformations SHOULD apply to re-loaded instances:
        val loadedAgain = AppCompatResources.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.gradientRadiusCompat).isEqualTo(0.5f)
    }

    @Test fun resource_withMutateTrue_resolvesWithContextAndMutates() {
        assumeFalse("XML drawable does not have correct radius on API 21", Build.VERSION.SDK_INT == 21)

        val deferred = DeferredDrawable.Resource(R.drawable.oval)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)

        resolved.gradientRadius = 0.4f

        // Since it's mutated, transformations SHOULD NOT apply to re-loaded instances:
        val loadedAgain = AppCompatResources.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)
    }

    @Test fun resource_withTransformations_resolvesWithContextAndMutatesAndAppliesTransformation() {
        val deferred = DeferredDrawable.Resource(R.drawable.oval) {
            require(this is GradientDrawable)
            gradientRadius = 0.3f
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(0.3f)
    }

    @Test fun attribute_withMutateFalse_resolvesWithContext() {
        assumeFalse("XML drawable does not have correct radius on API 21", Build.VERSION.SDK_INT == 21)

        context.setTheme(R.style.TestTheme)

        val deferred = DeferredDrawable.Attribute(android.R.attr.homeAsUpIndicator, mutate = false)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)

        resolved.gradientRadius = 0.5f

        // Since it's not mutated, transformations SHOULD apply to re-loaded instances:
        val loadedAgain = AppCompatResources.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.gradientRadiusCompat).isEqualTo(0.5f)
    }

    @Test fun attribute_withMutateTrue_resolvesWithContextAndMutates() {
        assumeFalse("XML drawable does not have correct radius on API 21", Build.VERSION.SDK_INT == 21)

        context.setTheme(R.style.TestTheme)

        val deferred = DeferredDrawable.Attribute(android.R.attr.homeAsUpIndicator)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)

        resolved.gradientRadius = 0.4f

        // Since it's mutated, transformations SHOULD NOT apply to re-loaded instances:
        val loadedAgain = AppCompatResources.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.gradientRadiusCompat).isEqualTo(defaultOvalGradientFraction)
    }

    @Test fun attribute_withTransformations_resolvesWithContextAndMutatesAndAppliesTransformation() {
        context.setTheme(R.style.TestTheme)

        val deferred = DeferredDrawable.Attribute(android.R.attr.homeAsUpIndicator) {
            require(this is GradientDrawable)
            gradientRadius = 0.3f
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.gradientRadiusCompat).isEqualTo(0.3f)
    }

    internal companion object {

        @JvmStatic internal val GradientDrawable.gradientRadiusCompat: Float
            @JvmName("getGradientRadiusCompat")
            get() = if (Build.VERSION.SDK_INT >= 21) gradientRadius else {
                getPrivateField<Any>("mGradientState").getPrivateField("mGradientRadius")
            }

        @Suppress("UNCHECKED_CAST")
        private fun <T : Any> Any.getPrivateField(name: String): T =
            javaClass.getDeclaredField(name)
                .apply { isAccessible = true }
                .get(this) as T
    }
}
