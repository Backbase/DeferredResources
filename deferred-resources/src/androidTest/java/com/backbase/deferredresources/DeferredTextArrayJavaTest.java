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

import com.backbase.deferredresources.test.R;
import com.backbase.deferredresources.test.TestContext;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeferredTextArrayJavaTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void constant_initializedWithArray_doesNotReflectLaterArrayChanges() {
        String[] originalArray = new String[]{"A"};
        DeferredTextArray deferred = new DeferredTextArray.Constant(originalArray);
        originalArray[0] = "Z";

        CharSequence[] resolved = deferred.resolve(TestContext.getContext());
        assertThat(Arrays.asList(resolved)).isEqualTo(Collections.singletonList("A"));
    }

    // This test is impossible in Kotlin because Kotlin prevents writing to an Array<out T> thanks to type projection
    @Test
    public void constant_arrayChangedAfterResolved_doesNotAffectRepeatedResolutions() {
        DeferredTextArray deferred = new DeferredTextArray.Constant("A");

        CharSequence[] resolved1 = deferred.resolve(TestContext.getContext());
        resolved1[0] = "B";

        CharSequence[] resolved2 = deferred.resolve(TestContext.getContext());
        assertThat(Arrays.asList(resolved2)).isEqualTo(Collections.singletonList("A"));
    }

    // This test is impossible in Kotlin because Kotlin prevents writing to an Array<out T> thanks to type projection
    @Test
    public void resource_arrayChangedAfterResolved_doesNotAffectRepeatedResolutions() {
        DeferredTextArray deferred = new DeferredTextArray.Resource(R.array.stringArray);

        CharSequence[] resolved1 = deferred.resolve(TestContext.getContext());
        resolved1[0] = "Wrong one";

        CharSequence[] resolved2 = deferred.resolve(TestContext.getContext());
        assertThat(resolved2[0]).isEqualTo("Bold one");
    }
}
