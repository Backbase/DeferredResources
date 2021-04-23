/*
 * Copyright 2020 Backbase R&D B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
