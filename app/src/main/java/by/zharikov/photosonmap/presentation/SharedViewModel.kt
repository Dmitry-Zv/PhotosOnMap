package by.zharikov.photosonmap.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _userState = MutableStateFlow("")
    val userState = _userState.asStateFlow()


    fun setUser(token: String) {
        viewModelScope.launch {
            _userState.emit(token)
        }
    }

}