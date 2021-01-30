package com.backbase.deferredresources.test.safeargs

import com.backbase.deferredresources.test.launchActivity
import com.google.common.truth.Truth.assertThat

internal fun <T : Any> sendAndReceiveWithSafeArgs(
    construct: () -> T,
    send: SendFragment.(T) -> Unit,
    receive: ReceiveFragment.() -> T?,
) {
    val scenario = launchActivity<SendReceiveActivity>()

    val sent: T = construct()
    scenario.onActivity { activity ->
        val sendFragment = activity.currentFragment<SendFragment>()
        sendFragment.send(sent)
    }

    scenario.onActivity { activity ->
        val receiveFragment = activity.currentFragment<ReceiveFragment>()
        val received = receiveFragment.receive()

        assertThat(received).isEqualTo(sent)
        assertThat(received!!.hashCode()).isEqualTo(sent.hashCode())
    }
}
