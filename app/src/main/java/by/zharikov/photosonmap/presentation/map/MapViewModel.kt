package by.zharikov.photosonmap.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.zharikov.photosonmap.domain.usecase.map.MapUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCases: MapUseCases
) : ViewModel(), Event<MapEvent> {

    private val _state = MutableStateFlow(MapState.default)
    val state = _state.asStateFlow()

    override fun onEvent(event: MapEvent) {
        when (event) {
            MapEvent.GetAllPhotos -> getAllPhotos()
            is MapEvent.GetPhotoById -> getPhotoById(photoId = event.id)
            is MapEvent.ShowError -> showError(exception = event.exception)
            is MapEvent.PerformCurrentLocation -> performLocation(
                location = LatLng(
                    event.latitude,
                    event.longitude
                )
            )
            MapEvent.PerformMinskLocation -> performLocation()
        }
    }

    private fun performLocation(location: LatLng = LatLng(53.903711, 27.554022)) {
        _state.value = _state.value.copy(location = location)
    }

    private fun getPhotoById(photoId: Int) {
        viewModelScope.launch {
            when (val result = mapUseCases.getPhotoById(photoId = photoId)) {
                is Resource.Error -> showError(exception = result.exception)
                is Resource.Success -> _state.value = _state.value.copy(photoUi = result.data)
            }
            performDefault()
        }
    }

    private fun showError(exception: Exception) {
        _state.value = _state.value.copy(msgError = exception.message ?: "Unknown error.")
    }

    private fun getAllPhotos() {

        viewModelScope.launch {
            when (val result = mapUseCases.getAllPhotos()) {
                is Resource.Error -> showError(exception = result.exception)
                is Resource.Success -> _state.value = _state.value.copy(listOfPhotoUi = result.data)
            }
            performDefault()
        }
    }

    private fun performDefault() {
        _state.value = MapState.default
    }


}