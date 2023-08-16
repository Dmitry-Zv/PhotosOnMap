package by.zharikov.photosonmap.domain.repository

import android.content.SharedPreferences

interface SharedPreferencesUserRepository {

    suspend fun getPreferences():SharedPreferences
}