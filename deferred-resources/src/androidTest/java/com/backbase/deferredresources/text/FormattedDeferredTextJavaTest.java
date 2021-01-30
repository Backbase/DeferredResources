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
