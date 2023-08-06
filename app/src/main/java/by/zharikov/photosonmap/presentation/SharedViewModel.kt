package by.zharikov.photosonmap.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SharedViewModel : ViewModel() {

    private val _data = MutableSharedFlow<User.Data>()
    val data = _data.asSharedFlow()


    fun setData(data: User.Data) {
        viewModelScope.launch {
            _data.emit(data)

        }
    }
}