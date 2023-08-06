package by.zharikov.photosonmap.presentation.authorization.signup


sealed class SignUpEvent {
    object Default : SignUpEvent()
    data class OnSignUp(val login: String, val password: String, val repeatPassword: String) :
        SignUpEvent()
}