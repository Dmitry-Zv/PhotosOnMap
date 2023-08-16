package by.zharikov.photosonmap.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.usecase.authentication.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel() {


    fun signOut() {
        viewModelScope.launch {
            authenticationUseCases.signOut()
        }
    }

}