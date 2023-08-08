package by.zharikov.photosonmap.presentation.detail

sealed class DetailEvent {
    object Default : DetailEvent()
    data class SendComment(val commentText: String, val imageId: Int) : DetailEvent()
    data class SendToken(val token: String) : DetailEvent()
    data class DeleteComment(val imageId: Int, val commentId: Int) : DetailEvent()
}
