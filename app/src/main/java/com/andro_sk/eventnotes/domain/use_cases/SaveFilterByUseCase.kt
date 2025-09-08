package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import javax.inject.Inject

class SaveFilterByUseCase @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) {
    suspend operator fun invoke(filterBy: String) {
        dataStoreRepositoryImpl.saveFilter(filterBy)
    }
}