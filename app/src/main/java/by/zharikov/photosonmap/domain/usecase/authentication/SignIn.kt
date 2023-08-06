package by.zharikov.photosonmap.domain.usecase.authentication

import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.utils.Resource
import javax.inject.Inject

class SignIn @Inject constructor(private val repository: AuthenticationRepository) {

    suspend operator fun invoke(login: String, password: String): Resource<User> {
        return try {
            if (login.isBlank()) throw Exception("Field login is empty.")
            if (password.isBlank()) throw Exception("Field password is empty.")

            repository.signIn(login = login, password = password)
        } catch (e: Exception) {
            Resource.Error(e)
        }

    }
}