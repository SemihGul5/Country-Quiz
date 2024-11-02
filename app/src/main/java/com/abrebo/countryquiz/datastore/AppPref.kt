package com.abrebo.countryquiz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppPref private constructor(context: Context) {

    private val dataStore: DataStore<Preferences> = context.ds

    companion object {
        private val Context.ds: DataStore<Preferences> by preferencesDataStore("bilgiler")
        private val USERNAME_KEY = stringPreferencesKey("UserName")

        @Volatile
        private var INSTANCE: AppPref? = null

        fun getInstance(context: Context): AppPref {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppPref(context).also { INSTANCE = it }
            }
        }
    }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = userName
        }
    }

    suspend fun getUserName(): String {
        val preferences = dataStore.data.first()
        return preferences[USERNAME_KEY] ?: "No have a UserName"
    }
}