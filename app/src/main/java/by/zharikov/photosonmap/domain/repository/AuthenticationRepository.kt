package by.zharikov.photosonmap.domain.repository

import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.utils.Resource

interface AuthenticationRepository {

    suspend fun signUp(login: String, password: String): Resource<User>

    suspend fun signIn(login: String, password: String): Resource<User>


}