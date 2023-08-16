package by.zharikov.photosonmap.presentation.authorization.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.SharedPreferencesUserRepository
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel(), Event<SignUpEvent> {

    private var _state = MutableStateFlow(SignUpState.default)
    val state = _state.asStateFlow()

    override fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.Default -> performDefault()
            is SignUpEvent.OnSignUp -> signUp(
                login = event.login,
                password = event.password,
                repeatPassword = event.repeatPassword
            )
        }
    }

    private fun signUp(login: String, password: String, repeatPassword: String) {
        _state.value = _state.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = authenticationUseCases.signUp(
                login = login,
                password = password,
                repeatPassword = repeatPassword
            )) {
                is Resource.Error -> showError(
                    msgError = result.exception.message ?: "Unknown error"
                )
                is Resource.Success -> {
                    authenticationUseCases.saveUser(user = result.data.data)
                    showSuccess(data = result.data)
                }
            }

        }
        performDefault()
    }

    private fun showSuccess(data: User) {

        _state.value = _state.value.copy(
            isLoading = false,
            data = data
        )
    }

    private fun showError(msgError: String) {
        _state.value = _state.value.copy(
            isLoading = false,
            msgError = msgError
        )
    }

    private fun performDefault() {
        _state.value = SignUpState.default
    }

}