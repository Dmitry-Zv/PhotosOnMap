package by.zharikov.photosonmap.domain.usecase.comment

import by.zharikov.photosonmap.domain.model.DeleteStatusDto
import by.zharikov.photosonmap.domain.repository.CommentRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class DeleteComments @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(
        token: String,
        imageId: Int,
        commentId: Int
    ): Resource<DeleteStatusDto> =
        repository.deleteComment(token, imageId, commentId)
}