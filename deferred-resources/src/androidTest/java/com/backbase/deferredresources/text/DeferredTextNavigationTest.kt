package com.backbase.deferredresources.text

import com.backbase.deferredresources.DeferredText
import com.backbase.deferredresources.test.launchActivity
import com.backbase.deferredresources.test.safeargs.ReceiveFragment
import com.backbase.deferredresources.test.safeargs.SendFragment
import com.backbase.deferredresources.test.safeargs.SendReceiveActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DeferredTextNavigationTest {

    @Test fun sendWorks() {
        val scenario = launchActivity<SendReceiveActivity>()

        val sent: ParcelableDeferredText = DeferredText.Constant("Hey")
        scenario.onActivity { activity ->
            val sendFragment = activity.currentFragment<SendFragment>()
            sendFragment.send(sent)
        }

        scenario.onActivity { activity ->
            val receiveFragment = activity.currentFragment<ReceiveFragment>()
            val received: ParcelableDeferredText = receiveFragment.getValue()

            assertThat(received).isEqualTo(sent)
        }
    }
}
