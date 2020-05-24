package com.backbase.deferredresources.text;

import com.backbase.deferredresources.DeferredFormattedPlurals;
import com.backbase.deferredresources.DeferredText;
import com.backbase.deferredresources.SpecificLocaleTest;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class QuantifiedFormattedDeferredTextJavaTest extends SpecificLocaleTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void quantifiedAndFormatted_initializedWithArray_doesNotReflectLaterArrayChanges() {
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
