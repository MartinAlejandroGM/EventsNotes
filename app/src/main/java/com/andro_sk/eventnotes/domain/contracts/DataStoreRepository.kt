package com.andro_sk.eventnotes.domain.contracts

import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.models.UserPreferences
import com.andro_sk.eventnotes.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveTheme(isDarkMode: Boolean): Response<Unit>
    suspend fun saveFilter(filterBy: String): Response<Unit>
    fun getUserPreferences(): Flow<UserPreferences>
    fun getUserSettings(): Flow<UserSettings>
}