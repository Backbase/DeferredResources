package com.backbase.deferredresources.drawable

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.context
import com.backbase.deferredresources.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredColorDrawableTest {

    @Test fun withDeferredColor_returnsDrawableWithResolvedColor() {
        val deferred = DeferredColorDrawable(DeferredColor.Resource(R.color.blue))

        val resolved = deferred.resolve(context) as ColorDrawable
        assertThat(resolved.color).isEqualTo(Color.BLUE)
    }
}
