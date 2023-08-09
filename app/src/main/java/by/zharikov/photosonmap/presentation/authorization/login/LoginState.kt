package by.zharikov.photosonmap.presentation.authorization.login

import by.zharikov.photosonmap.domain.model.User

data class LoginState(
    val data: User?,
    val msgError: String?,
    val isLoading: Boolean
) {
    companion object {
        val default = LoginState(
            data = null,
            msgError = null,
            isLoading = false
        )
    }
}