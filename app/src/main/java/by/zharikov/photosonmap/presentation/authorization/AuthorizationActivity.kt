package by.zharikov.photosonmap.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.zharikov.photosonmap.adapter.PagerAuthAdapter
import by.zharikov.photosonmap.databinding.ActivityAuthorizationBinding
import by.zharikov.photosonmap.presentation.MainActivity
import by.zharikov.photosonmap.utils.Constants
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private var _binding: ActivityAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel:AuthorizationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        collectState()
        setUpViewPager()
        setUpTabLayout()
    }

    private fun collectState() {
        collectLatestLifecycleFlow(viewModel.state){state->
            Log.d("AUTH_STATE", state.toString())
            when{
                state.user !=null -> {
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(Constants.DATA_KEY, state.user)
                        startActivity(this)
                    }
                    finish()
                }
            }
        }
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


}