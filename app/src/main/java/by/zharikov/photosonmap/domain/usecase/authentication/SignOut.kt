package by.zharikov.photosonmap.domain.usecase.authentication

import by.zharikov.photosonmap.domain.repository.AuthenticationRepository
import by.zharikov.photosonmap.domain.repository.SharedPreferencesUserRepository
import by.zharikov.photosonmap.utils.Constants
import javax.inject.Inject

class SignOut @Inject constructor(
    private val sharedPreferencesUserRepository: SharedPreferencesUserRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke() {
        sharedPreferencesUserRepository.getPreferences().edit()
            .remove(Constants.SHARED_PREFERENCES_USER_TOKEN)
            .apply()
        authenticationRepository.signOut()
    }
}