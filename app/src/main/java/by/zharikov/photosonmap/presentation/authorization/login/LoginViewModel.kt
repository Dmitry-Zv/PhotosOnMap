package by.zharikov.photosonmap.presentation.authorization.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel(), Event<LoginEvent> {


    private var _state = MutableStateFlow(LoginState.default)
    val state = _state.asStateFlow()

    override fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Default -> performDefault()
            is LoginEvent.OnLogin -> login(login = event.login, password = event.password)
        }
    }

    private fun login(login: String, password: String) {
        _state.value = _state.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = authenticationUseCases.signIn(login = login, password = password)) {
                is Resource.Error -> showError(
                    msgError = result.exception.message ?: "Unknown error"
                )
                is Resource.Success ->{
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
        _state.value = LoginState.default
    }


}