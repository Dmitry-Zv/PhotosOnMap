package by.zharikov.photosonmap.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.zharikov.photosonmap.domain.model.CommentUi
import by.zharikov.photosonmap.domain.usecase.comment.CommentUseCases
import by.zharikov.photosonmap.presentation.common.Event
import by.zharikov.photosonmap.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel @AssistedInject constructor(
    private val commentUseCases: CommentUseCases,
    @Assisted private val imageId: Int
) :
    ViewModel(), Event<DetailEvent> {

    private val tokenState = MutableStateFlow("")
    private var token = ""
    val commentFlow: Flow<PagingData<CommentUi>>

    init {
        commentFlow = tokenState.asStateFlow()
            .flatMapLatest {
                commentUseCases.getComments(token = it, imageId)

            }
            .cachedIn(viewModelScope)
    }


    private val _state = MutableStateFlow(DetailState.default)
    val state = _state.asStateFlow()

    override fun onEvent(event: DetailEvent) {
        when (event) {
            DetailEvent.Default -> performDefault()
            is DetailEvent.SendComment -> sendComment(
                commentText = event.commentText,
                imageId = event.imageId
            )
            is DetailEvent.SendToken -> sendToken(token = event.token)
            is DetailEvent.DeleteComment -> deleteComment(
                imageId = event.imageId,
                commentId = event.commentId
            )
        }
    }

    private fun deleteComment(imageId: Int, commentId: Int) {
        viewModelScope.launch {
            when (val result = commentUseCases.deleteComments(
                token = token,
                imageId = imageId,
                commentId = commentId
            )) {
                is Resource.Error -> _state.value = _state.value.copy(
                    msgError = result.exception.message ?: "Unknown error."
                )
                is Resource.Success -> _state.value = _state.value.copy(
                    status = result.data.status
                )
            }
            performDefault()
        }
    }

    private fun sendToken(token: String) {
        tokenState.value = token
        this.token = token
    }

    private fun sendComment(commentText: String, imageId: Int) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = commentUseCases.uploadComment(
                token = token,
                text = commentText,
                imageId = imageId
            )) {
                is Resource.Error -> _state.value = _state.value.copy(
                    msgError = result.exception.message ?: "Unknown error.",
                    isLoading = false
                )
                is Resource.Success -> _state.value = _state.value.copy(
                    data = result.data.comment,
                    isLoading = false
                )
            }

            performDefault()
        }
    }

    private fun performDefault() {
        _state.value = DetailState.default
    }

    @AssistedFactory
    interface Factory {


        fun create(imageId: Int): DetailViewModel
    }

}