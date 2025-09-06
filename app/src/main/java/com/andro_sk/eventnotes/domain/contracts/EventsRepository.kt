package com.andro_sk.eventnotes.domain.contracts

import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response

interface EventsRepository {
    suspend fun fetchEventById(eventId: String): Response<EventModel>
    suspend fun updateEventById(eventId: String): Response<EventModel>
    suspend fun deleteEventById(eventId: String): Response<Unit>
    suspend fun fetchEvents(): Response<List<EventModel>>
    suspend fun addEvent(event: EventModel): Response<Unit>
}
