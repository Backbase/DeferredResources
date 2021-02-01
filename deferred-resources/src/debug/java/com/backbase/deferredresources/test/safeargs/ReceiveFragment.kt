package com.backbase.deferredresources.test.safeargs

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.backbase.deferredresources.text.ParcelableDeferredFormattedPlurals
import com.backbase.deferredresources.text.ParcelableDeferredFormattedString
import com.backbase.deferredresources.text.ParcelableDeferredPlurals
import com.backbase.deferredresources.text.ParcelableDeferredText

// TODO WORKAROUND: Should be in androidTest/, but SafeArgs doesn't work there
internal class ReceiveFragment : Fragment() {

    private val args: ReceiveFragmentArgs by navArgs()

    fun getDeferredFormattedPlurals(): ParcelableDeferredFormattedPlurals? = args.deferredFormattedPlurals

    fun getDeferredFormattedStringArg(): ParcelableDeferredFormattedString? = args.deferredFormattedString

    fun getDeferredPlurals(): ParcelableDeferredPlurals? = args.deferredPlurals

    fun getDeferredTextArg(): ParcelableDeferredText? = args.deferredText
}
