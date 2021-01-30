package com.backbase.deferredresources.test.safeargs

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.backbase.deferredresources.test.R

internal class SendFragment : Fragment() {

    fun <T : Any> send(value: T) {
        findNavController().navigate(
            R.id.action_sendDestination_to_receiveDestination,
            bundleOf(ReceiveFragment.VALUE to value),
        )
    }
}
