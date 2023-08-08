package by.zharikov.photosonmap.presentation.detail

import by.zharikov.photosonmap.domain.model.Comment

data class DetailState(
    val status: Int?,
    val msgError: String?,
    val isLoading: Boolean,
    val data: Comment?
) {
    companion object {
        val default = DetailState(
            status = null,
            msgError = null,
            isLoading = false,
            data = null
        )
    }
}
