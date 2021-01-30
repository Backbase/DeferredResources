package com.backbase.deferredresources.text;

import com.backbase.deferredresources.DeferredFormattedPlurals;
import com.backbase.deferredresources.DeferredPlurals;
import com.backbase.deferredresources.test.SpecificLocaleTest;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class FormattedDeferredPluralsJavaTest extends SpecificLocaleTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void initializedWithArray_doesNotReflectLaterArrayChanges() {
        setTestLanguage("en");

        Object[] formatArgs = new String[]{"Original"};
        DeferredFormattedPlurals formattedPlurals = new DeferredFormattedPlurals.Constant("%s scones", "%s scones",
                "%s scone", "%s scones", "%s scones", "%s scones");
        DeferredPlurals deferred = new FormattedDeferredPlurals(formattedPlurals, formatArgs);
        formatArgs[0] = "Changed";

        assertThat(deferred.resolve(getContext(), 1)).isEqualTo("Original scone");
        assertThat(deferred.resolve(getContext(), 0)).isEqualTo("Original scones");
    }
}
