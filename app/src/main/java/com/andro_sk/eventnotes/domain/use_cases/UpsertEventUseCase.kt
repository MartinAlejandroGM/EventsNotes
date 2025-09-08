package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.EventNotesDbRepositoryImpl
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import javax.inject.Inject

class UpsertEventUseCase @Inject constructor(
    private val eventsRepository: EventNotesDbRepositoryImpl
) {
    suspend operator fun invoke(event: EventModel): Response<Long> =
        eventsRepository.upsertEvent(event)
}
