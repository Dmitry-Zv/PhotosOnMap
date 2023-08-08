package by.zharikov.photosonmap.domain.usecase.comment

data class CommentUseCases(
    val uploadComment: UploadComment,
    val getComments: GetComments,
    val deleteComments: DeleteComments
)