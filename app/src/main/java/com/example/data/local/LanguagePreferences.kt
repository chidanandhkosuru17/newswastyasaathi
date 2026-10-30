package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.i18n.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_language_prefs")

class LanguagePreferences(private val context: Context) {

    companion object {
        val SELECTED_LANGUAGE_KEY = stringPreferencesKey("selected_language_code")
    }

    val selectedLanguageFlow: Flow<AppLanguage> = context.languageDataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            val code = preferences[SELECTED_LANGUAGE_KEY]
            AppLanguage.fromCode(code)
        }

    suspend fun saveLanguage(language: AppLanguage) {
        context.languageDataStore.edit { preferences ->
            preferences[SELECTED_LANGUAGE_KEY] = language.code
        }
    }
}
