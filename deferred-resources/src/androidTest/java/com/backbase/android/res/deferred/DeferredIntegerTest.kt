package com.backbase.android.res.deferred

import com.backbase.android.res.deferred.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DeferredIntegerTest {

    @Test fun constant_returnsConstantValue() {
        val deferred = DeferredInteger.Constant(8723)
        assertThat(deferred.resolve(context)).isEqualTo(8723)
    }

    @Test fun resource_resolvesWithContext() {
        val deferred = DeferredInteger.Resource(R.integer.five)
        assertThat(deferred.resolve(context)).isEqualTo(5)
    }
}
