package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import com.andro_sk.eventnotes.domain.models.Response
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) {
    suspend operator fun invoke(isDarkMode: Boolean): Response<Unit> =
        dataStoreRepositoryImpl.saveTheme(isDarkMode)
}