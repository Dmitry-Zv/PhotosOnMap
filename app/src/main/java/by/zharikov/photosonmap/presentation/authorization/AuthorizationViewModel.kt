package by.zharikov.photosonmap.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import by.zharikov.photosonmap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(AuthorizationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = authenticationUseCases.getUser()) {
                is Resource.Error -> _state.value =
                    _state.value.copy(msgError = result.exception.message ?: "Unknown error.")
                is Resource.Success -> _state.value = _state.value.copy(user = result.data)
            }

        }
    }

}