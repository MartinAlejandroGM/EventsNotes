package com.andro_sk.eventnotes.domain.use_cases

import com.andro_sk.eventnotes.data.local.repository.FakeRepositoryImpl
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    val eventsRepository: FakeRepositoryImpl
) {
    operator fun invoke(event: EventModel): Flow<Response<Long>> = flow {
        when(val response = eventsRepository.addEvent(event)) {
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