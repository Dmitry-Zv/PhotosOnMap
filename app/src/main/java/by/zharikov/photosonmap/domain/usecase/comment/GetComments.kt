package by.zharikov.photosonmap.domain.usecase.comment

import androidx.paging.PagingData
import by.zharikov.photosonmap.domain.model.CommentUi
import by.zharikov.photosonmap.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetComments @Inject constructor(
    private val repository: CommentRepository
) {

    operator fun invoke(token: String, imageId: Int): Flow<PagingData<CommentUi>> =
        repository.getPagedComment(token, imageId)
}