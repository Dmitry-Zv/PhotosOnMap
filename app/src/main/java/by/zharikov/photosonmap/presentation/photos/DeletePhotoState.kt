package by.zharikov.photosonmap.presentation.photos

data class DeletePhotoState(
    val status: Int?,
    val msgError: String?
) {
    companion object {
        val default = DeletePhotoState(
            status = null,
            msgError = null
        )
    }
}
