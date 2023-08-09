package by.zharikov.photosonmap.presentation.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.zharikov.photosonmap.adapter.PagerAuthAdapter
import by.zharikov.photosonmap.databinding.ActivityAuthorizationBinding
import by.zharikov.photosonmap.presentation.authorization.signup.SignUpClickListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity(), SignUpClickListener {

    private var _binding: ActivityAuthorizationBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewPager()
        setUpTabLayout()
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                else -> tab.text = "Register"
            }
        }.attach()

    }

    private fun setUpViewPager() {
        val adapter = PagerAuthAdapter(
            supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSignUpClickListener() {
        binding.viewPager.currentItem = 0
    }
}