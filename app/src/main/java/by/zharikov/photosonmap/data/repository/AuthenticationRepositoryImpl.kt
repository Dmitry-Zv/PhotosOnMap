package by.zharikov.photosonmap.data.repository

import by.zharikov.photosonmap.data.network.PhotosApi
import by.zharikov.photosonmap.domain.model.PostAuthentication
import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.utils.Resource
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.log

class AuthenticationRepositoryImpl @Inject constructor(
    private val photosApi: PhotosApi
) : AuthenticationRepository {

    override suspend fun signUp(login: String, password: String): Resource<User> {
        return try {
            val response = photosApi.signUp(PostAuthentication(login, password))
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Resource.Success(data = user)
                } ?: throw Exception("User doesn't exist")
            } else {
                Resource.Error(exception = HttpException(response))
            }
        } catch (e: Exception) {
            Resource.Error(exception = e)
        }
    }

    override suspend fun signIn(login: String, password: String): Resource<User> {
        return try {
            val response = photosApi.signIn(PostAuthentication(login, password))
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Resource.Success(data = user)
                } ?: throw Exception("User doesn't exist")
            } else {
                Resource.Error(exception = HttpException(response))
            }
        } catch (e: Exception) {
            Resource.Error(exception = e)
        }
    }
}