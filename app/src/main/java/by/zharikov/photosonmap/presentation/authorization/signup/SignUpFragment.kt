package by.zharikov.photosonmap.presentation.authorization.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.zharikov.photosonmap.databinding.FragmentSignupBinding
import by.zharikov.photosonmap.presentation.MainActivity
import by.zharikov.photosonmap.utils.Constants
import by.zharikov.photosonmap.utils.collectLatestLifecycleFlow
import by.zharikov.photosonmap.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {
            signUp()
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
                        repeatPasswordEditText.text?.clear()
                        repeatPasswordLayout.clearFocus()
                    }
                }
                state.data != null -> {

                    with(binding) {
                        progressBar.visibility = View.GONE
                        loginEditText.text?.clear()
                        passwordEditText.text?.clear()
                        repeatPasswordEditText.text?.clear()
                    }
                    Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra(Constants.DATA_KEY, state.data.data)
                        startActivity(this)
                    }
                    requireActivity().finish()
                }
            }
        }
    }

    private fun signUp() {
        with(binding) {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()
            viewModel.onEvent(
                event = SignUpEvent.OnSignUp(
                    login = login,
                    password = password,
                    repeatPassword = repeatPassword
                )
            )

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}