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
