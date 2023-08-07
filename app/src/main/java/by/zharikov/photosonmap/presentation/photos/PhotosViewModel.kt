package by.zharikov.photosonmap.presentation.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.zharikov.photosonmap.domain.model.PhotoDto
import by.zharikov.photosonmap.domain.model.PhotoUi
import by.zharikov.photosonmap.domain.repository.PhotosRepository
import by.zharikov.photosonmap.domain.usecase.photos.PhotosUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosUseCases: PhotosUseCases,
) : ViewModel(), Event<PhotosEvent> {

    val photosFlow: Flow<PagingData<PhotoUi>>
    private var token = ""

    private val _state = MutableStateFlow(DeletePhotoState.default)
    val state = _state.asStateFlow()

    private val tokenState = MutableStateFlow("")

    init {
        photosFlow = tokenState.asStateFlow()
            .flatMapLatest {
                photosUseCases.getPhotos(token = it)

            }
            .cachedIn(viewModelScope)
    }

    override fun onEvent(event: PhotosEvent) {
        when (event) {
            is PhotosEvent.SetToken -> setToken(token = event.token)
            is PhotosEvent.DeletePhoto -> deletePhoto(photoId = event.photoId)
        }
    }

    private fun deletePhoto(photoId: Int) {
        viewModelScope.launch {
            when (val result = photosUseCases.deletePhoto(token = token, photoId = photoId)) {
                is Resource.Error -> _state.value =
                    _state.value.copy(msgError = result.exception.message ?: "Unknown error")
                is Resource.Success -> _state.value = _state.value.copy(
                    status = result.data.status
                )
            }



        }
        performDefault()
    }

    private fun performDefault(){
        _state.value = DeletePhotoState.default
    }

    private fun setToken(token: String) {
        tokenState.value = token
        this.token = token
    }

}