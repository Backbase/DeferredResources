package com.backbase.deferredresources;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.backbase.deferredresources.test.R;
import kotlin.Unit;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeferredDrawableJavaTest {

    @Test
    public void resourceConstructor_withTransformationFunction_syntaxWorks() {
        DeferredDrawable deferred = new DeferredDrawable.Resource(R.drawable.oval, (drawable, context) -> {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setGradientRadius(0.4f);
            return Unit.INSTANCE;
        });

        Drawable resolved = deferred.resolve(TestContext.getContext());
        assertThat(resolved).isInstanceOf(GradientDrawable.class);
        GradientDrawable resolvedGradient = (GradientDrawable) resolved;
        assertThat(DeferredDrawableTest.getGradientRadiusCompat(resolvedGradient)).isEqualTo(0.4f);
    }
}
