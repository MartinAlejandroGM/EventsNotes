package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.EventNotesDbRepositoryImpl
import com.andro_sk.eventnotes.data.local.repository.FakeRepositoryImpl
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchEventsUseCase @Inject constructor(
    val eventsRepository: EventNotesDbRepositoryImpl
) {
    operator fun invoke(filterBy: String): Flow<Response<List<EventModel>>> = flow {
        when(val response = eventsRepository.fetchEvents()) {
            is Response.Success -> {
                val listFiltered = when(filterBy) {
                    "date" -> {
                        response.data.sortedBy { it.date }
                    }
                    else -> {
                        response.data.sortedBy { it.eventTittle }
                    }
                }
                emit(Response.Success(listFiltered))
            }

            is Response.Error -> {
                emit(Response.Error(response.error))
            }
        }
    }.catch {
        emit(Response.Error(it))
    }
}