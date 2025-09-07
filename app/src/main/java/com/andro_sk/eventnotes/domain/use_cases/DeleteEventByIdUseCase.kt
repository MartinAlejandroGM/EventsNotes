package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.EventNotesDbRepositoryImpl
import com.andro_sk.eventnotes.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteEventByIdUseCase @Inject constructor(
    val eventsRepository: EventNotesDbRepositoryImpl
) {
    operator fun invoke(eventId: String): Flow<Response<Unit>> = flow {
        when(val response = eventsRepository.deleteEventById(eventId)) {
            is Response.Success -> {
                emit(Response.Success(response.data))
            }

            is Response.Error -> {
                emit(Response.Error(response.error))
            }
        }
    }.catch {
        emit(Response.Error(it))
    }
}