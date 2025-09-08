package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.EventNotesDbRepositoryImpl
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.ui.utils.getParsedDateOrDefaultDate
import javax.inject.Inject

class FetchEventsUseCase @Inject constructor(
    private val eventsRepository: EventNotesDbRepositoryImpl
) {
    suspend operator fun invoke(sortedBy: String): Response<List<EventModel>> =
        when(val response = eventsRepository.fetchEvents()) {
            is Response.Success -> {
                val sortedList = when(sortedBy) {
                    "date" -> {
                        response.data.sortedBy {
                            it.date.getParsedDateOrDefaultDate()
                        }
                    }
                    else -> {
                        response.data.sortedBy { it.eventTittle.lowercase() }
                    }
                }
                Response.Success(sortedList)
            }

            is Response.Error -> {
                Response.Error(response.error)
            }
        }
}