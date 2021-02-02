package com.backbase.deferredresources.test.safeargs

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.backbase.deferredresources.boolean.ParcelableDeferredBoolean
import com.backbase.deferredresources.text.ParcelableDeferredFormattedPlurals
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import com.backbase.deferredresources.text.ParcelableDeferredPlurals
import com.backbase.deferredresources.text.ParcelableDeferredText

// TODO WORKAROUND: Should be in androidTest/, but SafeArgs doesn't work there
internal class SendFragment : Fragment() {

    fun send(value: ParcelableDeferredBoolean) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(deferredBoolean = value))

    fun send(value: ParcelableDeferredFormattedPlurals) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(deferredFormattedPlurals = value))

    fun send(value: ParcelableDeferredFormattedString) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(deferredFormattedString = value))

    fun send(value: ParcelableDeferredPlurals) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(deferredPlurals = value))

    fun send(value: ParcelableDeferredText) =
        send(SendFragmentDirections.actionSendDestinationToReceiveDestination(deferredText = value))

    private fun send(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
