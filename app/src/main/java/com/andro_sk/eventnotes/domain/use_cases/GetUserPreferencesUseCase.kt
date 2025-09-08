package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import com.andro_sk.eventnotes.domain.models.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) {
    operator fun invoke(): Flow<UserPreferences> {
        return dataStoreRepositoryImpl.getUserPreferences()
    }
}