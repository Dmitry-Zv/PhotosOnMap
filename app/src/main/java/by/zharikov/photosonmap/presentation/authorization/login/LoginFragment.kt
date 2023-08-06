package by.zharikov.photosonmap.presentation.authorization.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.databinding.FragmentLoginBinding
import by.zharikov.photosonmap.presentation.MainActivity
import by.zharikov.photosonmap.utils.Constants.DATA_KEY
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showSnackBar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            login()
        }
        collectData()

    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.state) { state ->

            when {
                state.isLoading -> binding.progressBar.visibility = View.VISIBLE
                state.msgError != null -> {
                    showSnackBar(msg = state.msgError, binding.root)
                    with(binding) {
                        progressBar.visibility = View.GONE
                        loginEditText.text?.clear()
                        loginLayout.clearFocus()
                        passwordEditText.text?.clear()
                        passwordLayout.clearFocus()
                    }
                }
                state.data != null -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("STATE_DATA", state.data.toString())
                    Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra(DATA_KEY, state.data.data)
                        startActivity(this)
                    }
                    requireActivity().finish()
                }
            }
        }

    }

    private fun login() {
        with(binding) {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.onEvent(event = LoginEvent.OnLogin(login = login, password = password))
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}