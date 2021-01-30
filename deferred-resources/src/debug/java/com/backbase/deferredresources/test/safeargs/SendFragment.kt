package com.backbase.deferredresources.test.safeargs

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.backbase.deferredresources.text.ParcelableDeferredText

// TODO WORKAROUND: Should be in androidTest/, but SafeArgs doesn't work there
internal class SendFragment : Fragment() {

    fun send(value: ParcelableDeferredText) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(value))

    private fun send(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
