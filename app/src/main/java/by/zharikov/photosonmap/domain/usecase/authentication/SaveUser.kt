package by.zharikov.photosonmap.domain.usecase.authentication

import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.domain.repository.SharedPreferencesUserRepository
import by.zharikov.photosonmap.utils.Constants.SHARED_PREFERENCES_USER_TOKEN
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SaveUser @Inject constructor(private val sharedPreferencesUserRepository: SharedPreferencesUserRepository) {

    suspend operator fun invoke(user: User.Data){
        val jsonUser = Json.encodeToString(user)
        sharedPreferencesUserRepository.getPreferences().edit()
            .putString(SHARED_PREFERENCES_USER_TOKEN, jsonUser)
            .apply()
    }
}