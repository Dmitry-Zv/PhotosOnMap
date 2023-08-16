package by.zharikov.photosonmap.domain.usecase.authentication

import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.SharedPreferencesUserRepository
import by.zharikov.photosonmap.utils.Constants.SHARED_PREFERENCES_USER_TOKEN
import by.zharikov.photosonmap.utils.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class GetUser @Inject constructor(private val sharedPreferencesUserRepository: SharedPreferencesUserRepository) {

    suspend operator fun invoke(): Resource<User.Data> {
        return try {
            val userString = sharedPreferencesUserRepository.getPreferences()
                .getString(SHARED_PREFERENCES_USER_TOKEN, "")
            if (checkNotNull(userString).isEmpty()) {
                throw Exception("User is empty")
            } else {
                val user: User.Data =
                    Gson().fromJson(userString, object : TypeToken<User.Data>() {}.type)
                Resource.Success(data = user)
            }
        } catch (e: Exception) {
            Resource.Error(exception = e)
        }
    }
}