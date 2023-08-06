package by.zharikov.photosonmap.domain.usecase.authentication

import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class SignUp @Inject constructor(private val repository: AuthenticationRepository) {

    suspend operator fun invoke(
        login: String,
        password: String,
        repeatPassword: String
    ): Resource<User> {
        return try {
            if (login.isBlank()) throw Exception("Field login is empty.")
            if (password != repeatPassword) throw Exception("Fields password and repeat password don't match.")
            if (password.length < 8 || password.isBlank()) throw Exception("The length of password less than 8.")

            repository.signUp(login = login, password = password)
        } catch (e: Exception) {
            Resource.Error(exception = e)
        }
    }
}