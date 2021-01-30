package com.backbase.deferredresources;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.core.util.Preconditions;
import com.backbase.deferredresources.test.R;
import com.backbase.deferredresources.test.TestContext;
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
        Preconditions.checkNotNull(resolvedGradient);
        assertThat(DeferredDrawableTest.getGradientRadiusCompat(resolvedGradient)).isEqualTo(0.4f);
    }

    @Test
    public void attributeConstructor_withTransformationFunction_syntaxWorks() {
        DeferredDrawable deferred =
                new DeferredDrawable.Attribute(android.R.attr.homeAsUpIndicator, (drawable, context) -> {
                    GradientDrawable gradientDrawable = (GradientDrawable) drawable;
                    gradientDrawable.setGradientRadius(0.4f);
                    return Unit.INSTANCE;
                });

        Drawable resolved = deferred.resolve(TestContext.AppCompatContext(true));
        assertThat(resolved).isInstanceOf(GradientDrawable.class);
        GradientDrawable resolvedGradient = (GradientDrawable) resolved;
        Preconditions.checkNotNull(resolvedGradient);
        assertThat(DeferredDrawableTest.getGradientRadiusCompat(resolvedGradient)).isEqualTo(0.4f);
    }
}
