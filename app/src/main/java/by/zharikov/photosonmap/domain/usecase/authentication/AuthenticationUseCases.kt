package by.zharikov.photosonmap.domain.usecase.authentication

data class AuthenticationUseCases(
    val signUp: SignUp,
    val signIn: SignIn,
    val getUser: GetUser,
    val saveUser: SaveUser,
    val signOut: SignOut
    )
