package by.zharikov.photosonmap.presentation.camera

sealed class CameraEvent {
    data class SendPhoto(
        val base64Image: String,
        val latitude: Double,
        val longitude: Double,
        val currentTime: Long
    ) : CameraEvent()

    data class ShowError(val exception: Exception) : CameraEvent()

    data class SetToken(val token: String) : CameraEvent()
}