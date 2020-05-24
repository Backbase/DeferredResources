package com.backbase.deferredresources;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public final class DeferredTextJavaTest extends SpecificLocaleTest {

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void formatted_initializedWithArray_doesNotReflectLaterArrayChanges() {
        Object[] formatArgs = new String[]{"Original"};
        DeferredText deferred = new DeferredText.Formatted(new DeferredFormattedString.Constant("%s text"), formatArgs);
        formatArgs[0] = "Changed";

        CharSequence resolved = deferred.resolve(getContext());
        assertThat(resolved).isEqualTo("Original text");
    }

    // This test is impossible in Kotlin because Kotlin enforces spreading the original array when passed as a vararg
    @Test
    public void quantifiedAndFormatted_initializedWithArray_doesNotReflectLaterArrayChanges() {
        setTestLanguage("en");

        Object[] formatArgs = new Object[]{2, "small"};
        DeferredFormattedPlurals formattedPlurals = new DeferredFormattedPlurals.Constant("%d %s pandas",
                "%d %s pandas", "%d %s panda", "%d %s pandas", "%d %s pandas", "%d %s pandas");
        DeferredText deferred = new DeferredText.QuantifiedAndFormatted(formattedPlurals, 2, formatArgs);
        formatArgs[0] = 1;

        CharSequence resolved = deferred.resolve(getContext());
        assertThat(resolved).isEqualTo("2 small pandas");
    }
}
