package by.zharikov.photosonmap.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.usecase.photos.PhotosUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photosUseCases: PhotosUseCases
) : ViewModel(), Event<CameraEvent> {


    private var token = ""

    private val _state = MutableStateFlow(CameraState.default)
    val state = _state.asStateFlow()

    override fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.SendPhoto -> sendPhoto(
                base64Image = event.base64Image,
                latitude = event.latitude,
                longitude = event.longitude,
                currentTime = event.currentTime
            )
            is CameraEvent.ShowError -> showError(exception = event.exception)
            is CameraEvent.SetToken -> setToken(token = event.token)
        }
    }

    private fun setToken(token: String) {
        this.token = token
    }

    private fun showError(exception: Exception) {
        _state.value = _state.value.copy(msgError = exception.message ?: "Unknown error.")
    }

    private fun performDefault() {
        _state.value = CameraState.default
    }

    private fun sendPhoto(
        base64Image: String,
        latitude: Double,
        longitude: Double,
        currentTime: Long
    ) {

        viewModelScope.launch {
            when (val result = photosUseCases.uploadPhoto(
                token = token,
                base64Image = base64Image,
                latitude = latitude,
                longitude = longitude,
                currentTime = currentTime
            )) {
                is Resource.Error -> showError(result.exception)
                is Resource.Success -> showSuccess(data = result.data)
            }
            performDefault()
        }

    }

    private fun showSuccess(data: PhotoDto) {
        _state.value = _state.value.copy(
            data = data
        )
    }


}