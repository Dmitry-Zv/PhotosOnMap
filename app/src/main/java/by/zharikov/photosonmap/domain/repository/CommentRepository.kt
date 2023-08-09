package by.zharikov.photosonmap.domain.repository

import androidx.paging.PagingData
import by.zharikov.photosonmap.domain.model.CommentDto
import by.zharikov.photosonmap.domain.model.CommentUi
import by.zharikov.photosonmap.domain.model.DeleteStatusDto
import by.zharikov.photosonmap.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun uploadComment(
        text: String,
        token: String,
        imageId: Int
    ): Resource<CommentDto>

    fun getPagedComment(token: String, imageId: Int): Flow<PagingData<CommentUi>>

    suspend fun deleteComment(
        token: String,
        imageId: Int,
        commentId: Int
    ): Resource<DeleteStatusDto>


}