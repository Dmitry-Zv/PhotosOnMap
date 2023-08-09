package by.zharikov.photosonmap.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.zharikov.photosonmap.presentation.authorization.login.LoginFragment
import by.zharikov.photosonmap.presentation.authorization.signup.SignUpFragment


class PagerAuthAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            else -> SignUpFragment()
        }
    }


}