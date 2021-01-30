package com.backbase.deferredresources.test.safeargs

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.backbase.deferredresources.text.ParcelableDeferredText

// TODO WORKAROUND: Should be in androidTest/, but SafeArgs doesn't work there
internal class ReceiveFragment : Fragment() {

    private val args: ReceiveFragmentArgs by navArgs()

    fun getDeferredTextArg(): ParcelableDeferredText? = args.deferredText
}
