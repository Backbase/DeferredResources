package com.backbase.android.res.deferred

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.backbase.android.res.deferred.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test

class DeferredDrawableTest {

    /**
     * Some tests change drawables without mutating them, so the change persists. Reset after each test to avoid
     * messing with other tests.
     */
    @After fun restoreDrawables() {
        val loadedAgain = ContextCompat.getDrawable(context, R.drawable.oval) as GradientDrawable
        loadedAgain.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    }

    @Test fun constant_returnsConstantValue() {
        val drawable = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(Color.RED, Color.BLACK))
        val deferred = DeferredDrawable.Constant(drawable)
        assertThat(deferred.resolve(context)).isEqualTo(drawable)
    }

    @Test fun resource_withMutateFalse_resolvesWithContext() {
        val deferred = DeferredDrawable.Resource(R.drawable.oval, mutate = false)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.orientation).isEqualTo(GradientDrawable.Orientation.LEFT_RIGHT)

        resolved.orientation = GradientDrawable.Orientation.BOTTOM_TOP

        // Since it's not mutated, transformations SHOULD apply to re-loaded instances:
        val loadedAgain = ContextCompat.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.orientation).isEqualTo(GradientDrawable.Orientation.BOTTOM_TOP)
    }

    @Test fun resource_withMutateTrue_resolvesWithContextAndMutates() {
        val deferred = DeferredDrawable.Resource(R.drawable.oval)

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.orientation).isEqualTo(GradientDrawable.Orientation.LEFT_RIGHT)

        resolved.orientation = GradientDrawable.Orientation.BOTTOM_TOP

        // Since it's mutated, transformations SHOULD NOT apply to re-loaded instances:
        val loadedAgain = ContextCompat.getDrawable(context, R.drawable.oval) as GradientDrawable
        assertThat(loadedAgain.orientation).isEqualTo(GradientDrawable.Orientation.LEFT_RIGHT)
    }

    @Test fun resource_withTransformations_resolvesWithContextAndMutatesAndAppliesTransformation() {
        val deferred = DeferredDrawable.Resource(R.drawable.oval) {
            require(this is GradientDrawable)
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
        }

        val resolved = deferred.resolve(context)
        assertThat(resolved).isInstanceOf(GradientDrawable::class.java)
        resolved as GradientDrawable
        assertThat(resolved.orientation).isEqualTo(GradientDrawable.Orientation.TOP_BOTTOM)
    }
}
