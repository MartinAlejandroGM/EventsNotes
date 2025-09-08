package com.andro_sk.eventnotes.data.local.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.andro_sk.eventnotes.data.local.datastore.THEME_KEY
import com.andro_sk.eventnotes.data.local.datastore.SORT_BY_KEY
import com.andro_sk.eventnotes.data.local.datastore.dataStore
import com.andro_sk.eventnotes.domain.contracts.DataStoreRepository
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.models.UserPreferences
import com.andro_sk.eventnotes.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor (
    context: Context
): DataStoreRepository {
    private val dataStore = context.dataStore

    override suspend fun saveTheme(isDarkMode: Boolean): Response<Unit> =
        try {
            dataStore.edit { settings ->
                settings[THEME_KEY] = isDarkMode
            }
            Response.Success(Unit)
        } catch (exception: Exception) {
            Response.Error(exception)
        }


    override suspend fun saveFilter(filterBy: String): Response<Unit> =
        try {
            dataStore.edit { settings ->
                settings[SORT_BY_KEY] = filterBy
            }
            Response.Success(Unit)
        } catch (exception: Exception) {
            Response.Error(exception)
        }

    override fun getUserPreferences(): Flow<UserPreferences> =
         dataStore.data
            .map { preferences ->
                UserPreferences(preferences[SORT_BY_KEY] ?: "date")
            }
            .catch {
                UserPreferences( "date")
            }

    override fun getUserSettings(): Flow<UserSettings> =
        dataStore.data
            .map { preferences ->
                UserSettings(preferences[THEME_KEY] ?: false)
            }
            .catch {
                UserSettings( false)
            }
}