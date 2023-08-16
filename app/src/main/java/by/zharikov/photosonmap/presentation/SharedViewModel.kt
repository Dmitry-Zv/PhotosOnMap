package by.zharikov.photosonmap.presentation

import androidx.lifecycle.ViewModel
import by.zharikov.photosonmap.domain.model.PhotoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _userState = MutableStateFlow("")
    val userState = _userState.asStateFlow()

    private val _listOfPhotoUi = MutableStateFlow<List<PhotoUi>>(emptyList())
    val listOfPhotoUi = _listOfPhotoUi.asStateFlow()

    private val _photoUi = MutableStateFlow<PhotoUi>(PhotoUi())
    val photoUi = _photoUi.asStateFlow()


    fun setUser(token: String) {
        _userState.value = token
    }

    fun setListOfPhotoUi(listOfPhotoUi: List<PhotoUi>) {
        _listOfPhotoUi.value = listOfPhotoUi
    }

    fun setPhotoUi(photoUi:PhotoUi){
        _photoUi.value = photoUi
    }

}