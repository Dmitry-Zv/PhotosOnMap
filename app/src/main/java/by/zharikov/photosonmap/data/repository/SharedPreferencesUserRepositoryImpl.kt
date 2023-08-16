package by.zharikov.photosonmap.data.repository

import android.content.Context
import android.content.SharedPreferences
import by.zharikov.photosonmap.domain.repository.SharedPreferencesUserRepository
import by.zharikov.photosonmap.utils.Constants.SHARED_PREFERENCES_USER
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesUserRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SharedPreferencesUserRepository {
    override suspend fun getPreferences(): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_USER, Context.MODE_PRIVATE)

}