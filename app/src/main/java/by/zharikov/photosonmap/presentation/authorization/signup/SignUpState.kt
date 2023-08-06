package by.zharikov.photosonmap.presentation.authorization.signup

import by.zharikov.photosonmap.domain.model.User

data class SignUpState(
    val data: User?,
    val msgError: String?,
    val isLoading: Boolean
) {
    companion object {
        val default = SignUpState(
            data = null,
            msgError = null,
            isLoading = false
        )
    }
}
