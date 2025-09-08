package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import com.andro_sk.eventnotes.domain.models.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) {
    operator fun invoke(): Flow<UserSettings> {
        return dataStoreRepositoryImpl.getUserSettings()
    }
}