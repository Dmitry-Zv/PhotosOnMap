package by.zharikov.photosonmap.presentation.photos

sealed class PhotosEvent {
    data class SetToken(val token: String) : PhotosEvent()
    data class DeletePhoto(val photoId: Int) : PhotosEvent()
}
