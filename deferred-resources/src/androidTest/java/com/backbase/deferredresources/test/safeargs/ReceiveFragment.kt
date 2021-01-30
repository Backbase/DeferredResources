package com.backbase.deferredresources.test.safeargs

import androidx.fragment.app.Fragment

internal class ReceiveFragment : Fragment() {

    fun <T : Any> getValue(): T = checkNotNull(requireArguments().getParcelable(VALUE)) {
        "Never received a value"
    }

    internal companion object {
        internal const val VALUE = "ReceiveFragment.VALUE"
    }
}
