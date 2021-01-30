package com.backbase.deferredresources.test.safeargs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.backbase.deferredresources.test.R

internal class SendReceiveActivity : FragmentActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(FragmentContainerView(this)) {
            id = R.id.testContainer
            setContentView(this)
        }

        supportFragmentManager.commit {
            navHostFragment = NavHostFragment.create(R.navigation.parcelable)
            replace(R.id.testContainer, navHostFragment, "test_nav")
            setPrimaryNavigationFragment(navHostFragment)
        }
    }

    inline fun <reified F : Fragment> currentFragment(): F {
        val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
        check(currentFragment is F) {
            "Expected current fragment to be instance of <${F::class.java}>, instead got $currentFragment"
        }
        return currentFragment
    }
}
