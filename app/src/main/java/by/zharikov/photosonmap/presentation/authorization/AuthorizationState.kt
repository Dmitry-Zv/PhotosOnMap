package by.zharikov.photosonmap.presentation.authorization

import by.zharikov.photosonmap.domain.model.User

data class AuthorizationState(
    val user: User.Data? = null,
    val msgError:String? = null
)