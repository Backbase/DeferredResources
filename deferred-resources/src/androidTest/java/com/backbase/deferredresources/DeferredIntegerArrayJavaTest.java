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

import com.backbase.deferredresources.test.TestContext;
import java.util.Collections;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeferredIntegerArrayJavaTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void constant_initializedWithArray_doesNotReflectLaterArrayChanges() {
        int[] originalArray = new int[]{1};
        DeferredIntegerArray deferred = new DeferredIntegerArray.Constant(originalArray);
        originalArray[0] = 99;

        int[] resolved = deferred.resolve(TestContext.getContext());
        assertThat(resolved).asList().isEqualTo(Collections.singletonList(1));
    }
}
