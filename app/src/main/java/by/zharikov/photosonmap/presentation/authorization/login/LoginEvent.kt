package by.zharikov.photosonmap.presentation.authorization.login

sealed class LoginEvent {
    object Default : LoginEvent()
    data class OnLogin(val login: String, val password: String) : LoginEvent()

}
