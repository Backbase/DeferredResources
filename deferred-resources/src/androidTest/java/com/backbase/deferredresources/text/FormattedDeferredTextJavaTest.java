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

import com.backbase.deferredresources.DeferredFormattedString;
import com.backbase.deferredresources.DeferredText;
import org.junit.Test;

import static com.backbase.deferredresources.test.TestContext.getContext;
import static com.google.common.truth.Truth.assertThat;

public final class FormattedDeferredTextJavaTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void initializedWithArray_doesNotReflectLaterArrayChanges() {
        Object[] formatArgs = new String[]{"Original"};
        DeferredText deferred = new FormattedDeferredText(new DeferredFormattedString.Constant("%s text"), formatArgs);
        formatArgs[0] = "Changed";

        CharSequence resolved = deferred.resolve(getContext());
        assertThat(resolved).isEqualTo("Original text");
    }
}
