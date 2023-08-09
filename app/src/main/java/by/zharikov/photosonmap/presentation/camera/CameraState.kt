package by.zharikov.photosonmap.presentation.camera

import by.zharikov.photosonmap.domain.model.PhotoDto

data class CameraState(
    val msgError: String?,
    val data: PhotoDto?
) {
    companion object {
        val default = CameraState(
            msgError = null,
            data = null
        )
    }
}