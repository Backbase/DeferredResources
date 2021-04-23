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

package com.backbase.deferredresources.text;

import com.backbase.deferredresources.DeferredFormattedPlurals;
import com.backbase.deferredresources.DeferredText;
import com.backbase.deferredresources.test.SpecificLocaleTest;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class QuantifiedFormattedDeferredTextJavaTest extends SpecificLocaleTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void initializedWithArray_doesNotReflectLaterArrayChanges() {
        setTestLanguage("en");

        Object[] formatArgs = new Object[]{2, "small"};
        DeferredFormattedPlurals formattedPlurals = new DeferredFormattedPlurals.Constant("%d %s pandas",
                "%d %s pandas", "%d %s panda", "%d %s pandas", "%d %s pandas", "%d %s pandas");
        DeferredText deferred = new QuantifiedFormattedDeferredText(formattedPlurals, 2, formatArgs);
        formatArgs[0] = 1;

        CharSequence resolved = deferred.resolve(getContext());
        assertThat(resolved).isEqualTo("2 small pandas");
    }
}
